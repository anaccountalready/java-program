package five.edu.cn.model;

public interface NetworkListener {
    
    void onRemoteMove(int row, int col);
    
    void onRemoteUndo();
    
    void onRemoteChat(String message);
}
