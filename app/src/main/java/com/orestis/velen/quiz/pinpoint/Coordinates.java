package com.orestis.velen.quiz.pinpoint;

import android.support.v4.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Coordinates {

    private String city;
    private Map<String, Pair<Float, Float>> coords;

    public Coordinates(String city) {
        this.city = city;
        coords = new HashMap<>();
        addCapitals(coords);
        addMonuments(coords);
    }

    public float getWidthPercentage() {
        return coords.get(city.toLowerCase()).first;
    }

    public float getHeightPercentage() {
        return coords.get(city.toLowerCase()).second;
    }

    private void addCapitals(Map<String, Pair<Float, Float>> coords) {
        coords.put("beijing", new Pair<>(0.539205f, 0.328678f));
        coords.put("new Delhi", new Pair<>(0.298603f, 0.417472f));
        coords.put("washington", new Pair<>(0.767441f, 0.605135f));
        coords.put("jakarta", new Pair<>(0.479054f, 0.650912f));
        coords.put("brasilia", new Pair<>(0.680777f, 0.416377f));
        coords.put("islamabad", new Pair<>(0.279985f, 0.374507f));
        coords.put("abuja", new Pair<>(0.366433f, 0.377622f));
        coords.put("dhaka", new Pair<>(0.380236f, 0.446115f));
        coords.put("moscow", new Pair<>(0.066595f, 0.201217f));
        coords.put("mexico city", new Pair<>(0.595930f, 0.792151f));
        coords.put("tokyo", new Pair<>(0.673827f, 0.364482f));
        coords.put("addis ababa", new Pair<>(0.734965f, 0.367832f));
        coords.put("maynila", new Pair<>(0.566416f, 0.506265f));
        coords.put("hanoi", new Pair<>(0.473326f, 0.466165f));
        coords.put("cairo", new Pair<>(0.635664f, 0.109790f));
        coords.put("kinshasa", new Pair<>(0.467132f, 0.548951f));
        coords.put("tehran", new Pair<>(0.146795f, 0.364482f));
        coords.put("berlin", new Pair<>(0.448717f, 0.593688f));
        coords.put("ankara", new Pair<>(0.040816f, 0.334407f));
        coords.put("bangkok", new Pair<>(0.444683f, 0.514858f));
        coords.put("london", new Pair<>(0.226824f, 0.603550f));
        coords.put("paris", new Pair<>(0.269230f, 0.663708f));
        coords.put("rome", new Pair<>(0.446745f, 0.813609f));
        coords.put("dodoma", new Pair<>(0.708391f, 0.560139f));
        coords.put("pretoria", new Pair<>(0.617482f, 0.834265f));
        coords.put("naypyidaw", new Pair<>(0.417472f, 0.477622f));
        coords.put("seoul", new Pair<>(0.606516f, 0.353025f));
        coords.put("bogota", new Pair<>(0.378903f, 0.137404f));
        coords.put("nairobi", new Pair<>(0.714685f, 0.502797f));
        coords.put("madrid", new Pair<>(0.171597f, 0.849112f));
        coords.put("kiev", new Pair<>(0.737672f, 0.631163f));
        coords.put("buenos aires", new Pair<>(0.558639f, 0.657876f));
        coords.put("khartoum", new Pair<>(0.665034f, 0.293706f));
        coords.put("kampala", new Pair<>(0.667132f, 0.481118f));
        coords.put("algiers", new Pair<>(0.312587f, 0.007692f));
        coords.put("baghdad", new Pair<>(0.103831f, 0.385964f));
        coords.put("warsaw", new Pair<>(0.578895f, 0.589743f));
        coords.put("ottawa", new Pair<>(0.778585f, 0.532461f));
        coords.put("rabat", new Pair<>(0.202097f, 0.044755f));
        coords.put("kabul", new Pair<>(0.252774f, 0.371643f));
        coords.put("riyadh", new Pair<>(0.119584f, 0.444683f));
        coords.put("lima", new Pair<>(0.346287f, 0.349757f));
        coords.put("caracas", new Pair<>(0.471894f, 0.069396f));
        coords.put("kuala lumpur", new Pair<>(0.451843f, 0.585034f));
        coords.put("tashkent", new Pair<>(0.255639f, 0.322950f));
        coords.put("maputo", new Pair<>(0.667132f, 0.834265f));
        coords.put("kathmandu", new Pair<>(0.354457f, 0.421768f));
        coords.put("accra", new Pair<>(0.278321f, 0.416083f));
        coords.put("sana'a", new Pair<>(0.105263f, 0.507697f));
        coords.put("luanda", new Pair<>(0.440559f, 0.606293f));
        coords.put("antananarivo", new Pair<>(0.843356f, 0.732167f));
        coords.put("pyongyang", new Pair<>(0.597923f, 0.337271f));
        coords.put("canberra", new Pair<>(0.580277f, 0.652811f));
        coords.put("yaounde", new Pair<>(0.416083f, 0.439160f));
        coords.put("yamoussoukro", new Pair<>(0.225874f, 0.397202f));
        coords.put("niamey", new Pair<>(0.307692f, 0.315384f));
        coords.put("sri jayawardenepura kotte", new Pair<>(0.321518f, 0.559255f));
        coords.put("bucharest", new Pair<>(0.658777f, 0.756410f));
        coords.put("ouagadougou", new Pair<>(0.265734f, 0.329370f));
        coords.put("damascus", new Pair<>(0.060866f, 0.380236f));
    }

    private void addMonuments(Map<String, Pair<Float, Float>> coords) {
        //WestAsia
        coords.put("burj_al_arab_hotel", new Pair<>(0.559039f, 0.532491f));
        coords.put("mecca", new Pair<>(0.320609f, 0.603983f));
        coords.put("al_aqsa_mosque", new Pair<>(0.245518f, 0.409753f));
        coords.put("petra", new Pair<>(0.252593f, 0.437996f));
        String[] westAsiaMonuments = {"burj_al_arab_hotel", "mecca", "al_aqsa_mosque", "petra", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",};

        //Europe
        coords.put("eiffel_tower", new Pair<>(0.272038f, 0.516604f));
        coords.put("st_basil_catherdal", new Pair<>(0.745421f, 0.383164f));
        coords.put("blue_domed_hurch_santorini", new Pair<>(0.600576f, 0.731134f));
        coords.put("the_little_mermaid", new Pair<>(0.421268f, 0.384985f));
        coords.put("neptune_and_the_palace_of_versailles", new Pair<>(0.279112f, 0.519252f));
        coords.put("windmills_of_kinderdijk", new Pair<>(0.322388f, 0.461827f));
        coords.put("big_ben", new Pair<>(0.241132f, 0.464475f));
        coords.put("tower_of_pisa", new Pair<>(0.399217f, 0.617222f));
        coords.put("lascaux", new Pair<>(0.267611f, 0.588978f));
        coords.put("loch_ness", new Pair<>(0.193430f, 0.350563f));
        coords.put("mont_st_michel", new Pair<>(0.234927f, 0.525430f));
        coords.put("bran_castle", new Pair<>(0.598797f, 0.582800f));
        coords.put("agia_sophia", new Pair<>(0.649148f, 0.661408f));
        coords.put("brandenburg_gate", new Pair<>(0.431901f, 0.451236f));
        coords.put("acropolis", new Pair<>(0.581131f, 0.716130f));
        coords.put("sagrada_familia", new Pair<>(0.270259f, 0.656995f));
        coords.put("neuschwanstein", new Pair<>(0.399217f, 0.541317f));
        coords.put("stonehenge", new Pair<>(0.224335f, 0.477714f));
        coords.put("manneken_pis", new Pair<>(0.313534f, 0.486540f));
        coords.put("st_peter_cathedral", new Pair<>(0.431901f, 0.648169f));
        coords.put("trevi_fountain", new Pair<>(0.425695f, 0.648169f));
        coords.put("auschwitz", new Pair<>(0.526396f, 0.497131f));
        coords.put("north_cape", new Pair<>(0.596150f, 0.083021f));
        String[] europeMonuments = {"eiffel_tower", "st_basil_catherdal", "blue_domed_hurch_santorini", "the_little_mermaid", "neptune_and_the_palace_of_versailles",
                "windmills_of_kinderdijk", "big_ben", "tower_of_pisa", "lascaux", "loch_ness", "mont_st_michel", "bran_castle", "agia_sophia", "brandenburg_gate",
                "acropolis", "sagrada_familia", "neuschwanstein", "stonehenge", "manneken_pis", "st_peter_cathedral", "trevi_fountain", "auschwitz", "north_cape", "", "", "", "", "", "", "",};

        //Africa
        coords.put("the_great_sphynx_giza", new Pair<>(0.594371f, 0.214585f));
        coords.put("giza_pyramids", new Pair<>(0.594371f, 0.212820f));
        coords.put("victoria_falls", new Pair<>(0.545799f, 0.748786f));
        coords.put("cape_of_good_hope", new Pair<>(0.468971f, 0.943954f));
        coords.put("table_mountain", new Pair<>(0.471619f, 0.948367f));
        String[] africaMonuments = {"the_great_sphynx_giza", "giza_pyramids", "victoria_falls", "cape_of_good_hope", "table_mountain", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",};

        //NorthAmerica
        coords.put("statue_of_liberty", new Pair<>(0.789566f, 0.363802f));
        coords.put("capitol_hill", new Pair<>(0.745421f, 0.402692f));
        coords.put("niagara_falls", new Pair<>(0.725976f, 0.328497f));
        coords.put("mount_rushmore", new Pair<>(0.452215f, 0.304667f));
        coords.put("grand_canyon", new Pair<>(0.309107f, 0.448588f));
        coords.put("chichen_itza", new Pair<>(0.604962f, 0.670234f));
        coords.put("inukshuk", new Pair<>(0.723329f, 0.319671f));
        String[] northAmericaMonuments = {"statue_of_liberty", "capitol_hill", "niagara_falls", "mount_rushmore", "grand_canyon", "chichen_itza", "inukshuk", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",};

        //EastAsia
        coords.put("the_great_wall", new Pair<>(0.559039f, 0.300254f));
        coords.put("the_taj_mahal", new Pair<>(0.129841f, 0.482127f));
        coords.put("mount_fuji", new Pair<>(0.817823f, 0.363802f));
        coords.put("angkor_wat", new Pair<>(0.431901f, 0.652582f));
        coords.put("mount_everest", new Pair<>(0.236706f, 0.457414f));
        coords.put("the_great_buddha_of_kamakura", new Pair<>(0.824898f, 0.365567f));
        String[] eastAsiaMonuments = {"the_great_wall", "the_taj_mahal", "mount_fuji", "angkor_wat", "mount_everest", "the_great_buddha_of_kamakura", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",};

        //SouthAmerica
        coords.put("machu_picchu", new Pair<>(0.385978f, 0.398279f));
        coords.put("christ_the_redeemer", new Pair<>(0.690644f, 0.525430f));
        coords.put("easter_island", new Pair<>(0.149286f, 0.648169f));
        coords.put("nevado_mismi", new Pair<>(0.390363f, 0.422992f));
        String[] southAmericaMonuments = {"machu_picchu", "christ_the_redeemer", "easter_island", "nevado_mismi", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",};

        //Australia
        coords.put("uluru", new Pair<>(0.583779f, 0.685238f));
        String[] australiaMonuments = {"uluru", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",};
    }
}
