package IntelliForge.Helper.ForgeData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mincrmatt12. Do not copy this or you will have to face
 * our legal team. (dmf444)
 */
public class ParseCollection {

    public Map<String, Parser> versions = new HashMap<>();

    public BuildData getRecommended(String mcVersion){

        if (versions.get(mcVersion).promoVersions.size() == 2){
            return versions.get(mcVersion).datas.get(versions.get(mcVersion).promoVersions.get(1));
        }
        else {
            return null;
        }


    }

    public boolean hasRecommended(String mcVersion){
        return getRecommended(mcVersion) != null;
    }

    public BuildData getLatest(String mcVersion){

        if (versions.get(mcVersion).promoVersions.size() >= 1){
            return versions.get(mcVersion).datas.get(versions.get(mcVersion).promoVersions.get(0));
        }
        else {
            return null;
        }


    }

    public BuildData getVersion(String forgeVersion ) {

        forgeVersion = forgeVersion.trim();

        for (Parser b : this.versions.values()){



            if (b.datas.containsKey(forgeVersion)){
                return b.datas.get(forgeVersion);
            }

        }
        return null;

    }

    public Set<String> getMcVersionsLoaded(){

        return this.versions.keySet();

    }

    public boolean hasLatest(String mcVersion){
        return getLatest(mcVersion) != null;

    }

    private String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public ParseCollection(VersionPolicy policy) {

        String baseData = "http://files.minecraftforge.net/maven/net/minecraftforge/forge/index.json";

        try {
            String in = readUrl(baseData);

            Parser base = new Parser();
            base.run(in, true); //Parses the page, maybe should be threaded? (if so, the enitre class should be to avoid

            versions.put(base.version, base);

            for (String version : base.mcversions){

                if (policy.downloadMcVersion(version)){

                    Parser base2 = new Parser();

                    baseData = "http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_" + version + ".json";
                    in = readUrl(baseData);

                    base2.run(in, false);

                    versions.put(base2.version, base2);

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static interface VersionPolicy {

        public boolean downloadMcVersion(String version);

    }


}
