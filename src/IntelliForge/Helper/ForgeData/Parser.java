package IntelliForge.Helper.ForgeData;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

/**
 * Created by mincrmatt12. Do not copy this or you will have to face
 * our legal team. (dmf444)
 */
public class Parser {

    public Map<String, BuildData> datas = new LinkedHashMap<>();

    public String version;

    public String basePath;

    public ArrayList<String> promoVersions = new ArrayList<>();

    // Remember
    public ArrayList<String> mcversions;

    public void run(String in, boolean logExtra){


        JsonParser jsonParser = new JsonParser();



        JsonObject rootObj = jsonParser.parse(in).getAsJsonObject();

        mcversions = new ArrayList<>();

        Iterator<JsonElement> mcversionstt = rootObj.getAsJsonArray("mcversions").iterator();
        while (mcversionstt.hasNext()) {
            JsonElement next = mcversionstt.next();

            mcversions.add(next.getAsString());

        }

        JsonObject techRootOnj = rootObj.getAsJsonObject("md");

        String myVersion = techRootOnj.get("mcver").getAsString();

        this.version = myVersion;

        if (techRootOnj.getAsJsonObject("promos").has(myVersion)) {

            JsonObject thePromos = techRootOnj.getAsJsonObject("promos").getAsJsonObject(myVersion);

            if (thePromos.has("LATEST")){
                promoVersions.add(thePromos.get("LATEST").getAsString());

                if (thePromos.has("RECOMMENDED")){
                    promoVersions.add(thePromos.get("RECOMMENDED").getAsString());
                }
            }



        }

        String basename = "files.minecraftforge.net/maven";

        basename = basename + techRootOnj.get("path").getAsString();

        this.basePath = basename;

        JsonArray versions = techRootOnj.getAsJsonArray("versions");
        Iterator<JsonElement> versionsiter = versions.iterator();

        ArrayList<BuildData> bdatasTemp = new ArrayList<>();

        while (versionsiter.hasNext()) {
            JsonElement jsonElement = versionsiter.next();
            JsonObject theVersion = jsonElement.getAsJsonObject();

            BuildData dta = new BuildData();
            dta.bid = theVersion.get("build").getAsInt();

            JsonObject classes = theVersion.getAsJsonObject("classifiers");
            JsonObject src = classes.getAsJsonObject("src");

            dta.downloadLink = basePath + src.get("path").getAsString();

            dta.version = theVersion.get("version").getAsString();

            dta.mcversion = theVersion.get("mcversion").getAsString();

            if (theVersion.has("marker")){

                String mark = theVersion.get("marker").getAsString();

                switch (mark){
                    case "LATEST":

                        dta.isLatest = true;
                        dta.isRec = false;

                        break;

                    case "RECOMMENDED":

                        dta.isRec = true;
                        dta.isLatest = false;

                        break;
                }


            }

            bdatasTemp.add(dta);

        }

        Collections.sort(bdatasTemp);

        for (BuildData bdata : bdatasTemp){
            datas.put(bdata.version, bdata);
        }




    }

}
