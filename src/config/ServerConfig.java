package config;

/**
 * The ServerConfig class provides a singleton configuration for the server.
 * It holds configuration parameters such as the port number.
 */
public class ServerConfig {

    private static ServerConfig instance; 

    private int port; 
    // to be added  

    /**
     * Gets the singleton instance of ServerConfig.
     *
     * @return the single instance of ServerConfig
     */
    public static ServerConfig getInstance(){
        if(instance == null){
           return new ServerConfig(); 
        }
        else{
            System.out.println("Already created");
            return instance; 
        }

    }

    // Private constructor to prevent instantiation
    private ServerConfig(){
        port = 8080; // Default port
    }

    /**
     * Sets the port number for the server.
     *
     * @param portNumber the new port number
     */
    public void setPort(int portNumber){
        port = portNumber; 
    }

    /**
     * Gets the current port number.
     *
     * @return the current port number
     */
    public int getPort(){
        return port; 
    }

    
}
