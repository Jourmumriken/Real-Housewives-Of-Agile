package config;

public class serverConfig {

    private static serverConfig instance; 

    private int port; 
    // to be added  

    //singelton pattern
    public static serverConfig getInstance(){
        if(instance == null){
           return new serverConfig(); 
        }
        else{
            System.out.println("Already created");
            return instance; 
        }

    }

    private serverConfig(){
        port = 8080; 
    }

    public void setPort(int portNumber){
        port = portNumber; 
    }
    public int getPort(){
        return port; 
    }

    
}
