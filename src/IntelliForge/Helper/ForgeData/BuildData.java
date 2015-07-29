package IntelliForge.Helper.ForgeData;

/**
 * Created by mincrmatt12. Do not copy this or you will have to face
 * our legal team. (dmf444)
 */
public class BuildData implements Comparable<BuildData> {

    public String downloadLink = "";

    public String version= "";

    public String mcversion;

    public boolean isRec;

    public int bid;

    public boolean isLatest;

    @Override
    public int compareTo(BuildData o) {
        return o.bid - bid;
    }
}
