package IntelliForge.Helper;

public class OperatingSystemHelper {
    public static OperatingSystemHelper systemHelper = new OperatingSystemHelper();
    public static final String windows = "cmd /c gradlew.bat";
    public static final String mac = "bash gradlew";
    public static final String linux = "./gradlew";
    private static OSTYPE ostype;

    public OperatingSystemHelper(){
        ostype = OSTYPE.getOS(System.getProperty("os.name").toLowerCase());
    }

    public static String getOSexecuteString(){
        if(ostype == OSTYPE.WIN){
            return windows;
        } else if(ostype == OSTYPE.MAC){
            return mac;
        }else if(ostype == OSTYPE.LINUX){
            return linux;
        }else{
            return null;
        }
    }

    public static boolean isWindows(){
        return ostype == OSTYPE.WIN;
    }


    public static enum OSTYPE {
        WIN("win"),
        MAC("mac"),
        LINUX("nux");

        OSTYPE(String shortname){
            this.name = shortname;
        }
        private String name;

        public static OSTYPE getOS(String os){
            if(os.indexOf(WIN.name) >= 0)
                return WIN;
            else if(os.indexOf(MAC.name) >= 0)
                return MAC;
            else if(os.indexOf(LINUX.name) >= 0)
                return LINUX;
            return null;
        }
    }
}
