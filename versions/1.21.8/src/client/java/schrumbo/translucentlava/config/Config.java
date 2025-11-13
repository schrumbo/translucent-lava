package schrumbo.translucentlava.config;

import com.google.gson.annotations.SerializedName;

public class Config {
    public boolean enabled = false;
    public boolean getEnabled(){
        return enabled;
    }
    public void setEnabled(boolean value){
        enabled = value;
    }
}
