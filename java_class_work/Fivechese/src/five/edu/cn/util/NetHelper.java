package five.edu.cn.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import five.edu.cn.model.NetworkListener;

public class NetHelper implements Closeable {
    private static final Logger LOGGER = Logger.getLogger(NetHelper.class.getName());
    public static final int DEFAULT_PORT = 8900;
    private static final int MAX_MESSAGE_LENGTH = 1024;
    private static final Pattern IP_PATTERN = Pattern.compile(
        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^localhost$"
    );

    private static volatile NetHelper instance;
    private static final Object LOCK = new Object();

    private final ExecutorService executorService;
    private final AtomicBoolean isConnected;
    private final AtomicBoolean isListening;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread readThread;
    private Thread listenThread;
    private NetworkListener listener;

    private NetHelper() {
        this.executorService = Executors.newCachedThreadPool();
        this.isConnected = new AtomicBoolean(false);
        this.isListening = new AtomicBoolean(false);
    }

    public static NetHelper getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new NetHelper();
                }
            }
        }
        return instance;
    }

    public void setNetworkListener(NetworkListener listener) {
        this.listener = listener;
    }

    public void beginListen() {
        if (isListening.get()) {
            LOGGER.warning("Already listening");
            return;
        }

        listenThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(DEFAULT_PORT);
                isListening.set(true);
                LOGGER.info("Server listening on port " + DEFAULT_PORT);

                while (!Thread.currentThread().isInterrupted() && isListening.get()) {
                    try {
                        clientSocket = serverSocket.accept();
                        LOGGER.info("Client connected: " + clientSocket.getInetAddress());
                        initializeStreams();
                        startReadThread();
                    } catch (SocketException e) {
                        if (!isListening.get()) {
                            LOGGER.info("Server stopped listening");
                            break;
                        }
                        LOGGER.log(Level.SEVERE, "Socket error during accept", e);
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to start server", e);
            } finally {
                isListening.set(false);
            }
        }, "NetHelper-ListenThread");
        listenThread.start();
    }

    public void connect(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            LOGGER.warning("IP address is null or empty");
            return;
        }

        String trimmedIp = ip.trim();
        if (!isValidIpAddress(trimmedIp)) {
            LOGGER.warning("Invalid IP address format: " + trimmedIp);
            return;
        }

        if (isConnected.get()) {
            LOGGER.warning("Already connected");
            return;
        }

        executorService.execute(() -> {
            try {
                clientSocket = new Socket(trimmedIp, DEFAULT_PORT);
                initializeStreams();
                isConnected.set(true);
                LOGGER.info("Connected to server: " + trimmedIp);
                startReadThread();
            } catch (UnknownHostException e) {
                LOGGER.log(Level.SEVERE, "Unknown host: " + trimmedIp, e);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to connect to " + trimmedIp, e);
            }
        });
    }

    private void initializeStreams() throws IOException {
        if (clientSocket == null || clientSocket.isClosed()) {
            throw new IOException("Socket is not available");
        }
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
        writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
    }

    private void startReadThread() {
        readThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && isConnected.get()) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        LOGGER.info("Connection closed by remote");
                        break;
                    }
                    if (line.length() > MAX_MESSAGE_LENGTH) {
                        LOGGER.warning("Message too long, ignoring");
                        continue;
                    }
                    processMessage(line);
                } catch (SocketException e) {
                    if (!isConnected.get()) {
                        LOGGER.info("Read thread stopped");
                        break;
                    }
                    LOGGER.log(Level.SEVERE, "Socket error during read", e);
                    break;
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error reading from socket", e);
                    break;
                }
            }
            closeResources();
        }, "NetHelper-ReadThread");
        readThread.start();
    }

    private void processMessage(String line) {
        if (line == null || line.isEmpty()) {
            return;
        }

        try {
            if (line.startsWith("PutChess:")) {
                parseChessMessage(line);
            } else if (line.startsWith("chat")) {
                parseChatMessage(line);
            } else if ("reback".equals(line)) {
                if (listener != null) {
                    listener.onRemoteUndo();
                }
            } else {
                LOGGER.warning("Unknown message type: " + line.substring(0, Math.min(20, line.length())));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing message", e);
        }
    }

    private void parseChessMessage(String line) {
        try {
            String content = line.substring(9);
            String[] parts = content.split(",");
            if (parts.length != 2) {
                LOGGER.warning("Invalid chess message format");
                return;
            }
            int row = Integer.parseInt(parts[0].trim());
            int col = Integer.parseInt(parts[1].trim());
            
            if (row < 0 || row >= 19 || col < 0 || col >= 19) {
                LOGGER.warning("Invalid chess position: row=" + row + ", col=" + col);
                return;
            }
            
            if (listener != null) {
                listener.onRemoteMove(row, col);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid number format in chess message", e);
        }
    }

    private void parseChatMessage(String line) {
        try {
            String content = line.substring(4);
            if (content != null && !content.isEmpty() && listener != null) {
                listener.onRemoteChat(content);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error parsing chat message", e);
        }
    }

    public void sendChessMove(final int row, final int col) {
        if (!isConnected.get() || writer == null) {
            LOGGER.warning("Not connected, cannot send chess move");
            return;
        }
        if (row < 0 || row >= 19 || col < 0 || col >= 19) {
            LOGGER.warning("Invalid chess position");
            return;
        }

        executorService.execute(() -> {
            try {
                writer.println("PutChess:" + row + "," + col);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to send chess move", e);
            }
        });
    }

    public void sendUndoRequest() {
        if (!isConnected.get() || writer == null) {
            LOGGER.warning("Not connected, cannot send undo request");
            return;
        }

        executorService.execute(() -> {
            try {
                writer.println("reback");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to send undo request", e);
            }
        });
    }

    public void sendChatMessage(final String text) {
        if (!isConnected.get() || writer == null) {
            LOGGER.warning("Not connected, cannot send chat message");
            return;
        }
        if (text == null || text.isEmpty()) {
            return;
        }

        final String safeText = text.length() > MAX_MESSAGE_LENGTH 
            ? text.substring(0, MAX_MESSAGE_LENGTH) 
            : text;

        executorService.execute(() -> {
            try {
                writer.println("chat" + safeText);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to send chat message", e);
            }
        });
    }

    private boolean isValidIpAddress(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        return "localhost".equalsIgnoreCase(ip) || IP_PATTERN.matcher(ip).matches();
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public boolean isListening() {
        return isListening.get();
    }

    private void closeResources() {
        isConnected.set(false);
        isListening.set(false);

        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Error closing reader", e);
        }

        try {
            if (writer != null) {
                writer.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Error closing writer", e);
        }

        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Error closing client socket", e);
        }

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Error closing server socket", e);
        }
    }

    @Override
    public void close() {
        closeResources();
        executorService.shutdown();
        instance = null;
    }
}
