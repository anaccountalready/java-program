package five.edu.cn;

public class ClientInfo {
    public static final int ROLE_PLAYER_BLACK = 1;
    public static final int ROLE_PLAYER_WHITE = 2;
    public static final int ROLE_SPECTATOR = 3;
    
    private String userName;
    private int role;
    private boolean ready;
    
    public ClientInfo(String userName, int role) {
        this.userName = userName;
        this.role = role;
        this.ready = false;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public int getRole() {
        return role;
    }
    
    public void setRole(int role) {
        this.role = role;
    }
    
    public boolean isReady() {
        return ready;
    }
    
    public void setReady(boolean ready) {
        this.ready = ready;
    }
    
    public String getRoleName() {
        switch(role) {
            case ROLE_PLAYER_BLACK:
                return "黑棋玩家";
            case ROLE_PLAYER_WHITE:
                return "白棋玩家";
            case ROLE_SPECTATOR:
                return "观众";
            default:
                return "未知";
        }
    }
}
