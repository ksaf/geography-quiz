package com.orestis.velen.quiz.geography;

import com.orestis.velen.quiz.R;

import java.util.Arrays;
import java.util.List;

public class Continents {

    private static final String[] africa = {"abuja","accra","addis ababa","algiers","antananarivo","asmara","bamako","bangui","banjul","bissau","brazzaville","bujumbura","cairo","cape town","bloemfontein","pretoria","conakry","dakar","dar es salaam",
            "djibouti","freetown","gaborone","harare","juba","kampala","khartoum","kigali","kinshasa","el aaiún","libreville","lilongwe","mbabane","lome","luanda","lusaka","malabo","maputo","maseru","mogadishu","monrovia","moroni","nairobi","n'djamena","niamey",
            "nouakchott","ouagadougou","port louis","porto-novo","cotonou","praia","rabat","tripoli","tunis","victoria","windhoek","yamoussoukro","yaounde","dodoma","oyala","são tomé"
    };

    private static final String[] africaMonuments = {"the_great_sphynx_giza", "giza_pyramids", "victoria_falls", "cape_of_good_hope", "table_mountain"};

    private static final String[] northAmerica = {"saint john's","nassau","bridgetown","belmopan","ottawa","san jose","havana","roseau","santo domingo","san salvador","saint george's","guatemala city","port-au-prince","tegucigalpa","kingston",
            "mexico city","managua","panama city","basseterre","castries","kingstown","port of spain","washington"
    };

    private static final String[] northAmericaMonuments = {"statue_of_liberty", "capitol_hill", "niagara_falls", "mount_rushmore", "grand_canyon", "chichen_itza", "inukshuk"};

    private static final String[] southAmerica = {"buenos aires","sucre","la paz","brasilia","santiago","bogota","quito","georgetown","asunción","lima","paramaribo","montevideo","caracas"
    };

    private static final String[] southAmericaMonuments = {"machu_picchu", "christ_the_redeemer", "easter_island", "nevado_mismi"};

    private static final String[] europe = {"tirana","andorra la vella","vienna","minsk","brussels","sarajevo","sofia","zagreb","nicosia","prague","copenhagen","tallinn","helsinki","paris","berlin","athens","budapest",
            "reykjavik","dublin","rome","nur-sultan","pristina","riga","vaduz","vilnius","luxembourg (city)","valletta","chisinau","monaco","podgorica","amsterdam","skopje","oslo","warsaw","lisbon","bucharest","san marino","belgrade","bratislava",
            "ljubljana","madrid","stockholm","bern","kiev","london","vatican city"
    };

    private static final String[] europeMonuments = {"eiffel_tower", "st_basil_catherdal", "blue_domed_hurch_santorini", "the_little_mermaid", "neptune_and_the_palace_of_versailles",
            "windmills_of_kinderdijk", "big_ben", "tower_of_pisa", "lascaux", "loch_ness", "mont_st_michel", "bran_castle", "agia_sophia", "brandenburg_gate",
            "acropolis", "sagrada_familia", "neuschwanstein", "stonehenge", "manneken_pis", "st_peter_cathedral", "trevi_fountain", "auschwitz", "north_cape"};

    private static final String[] eastAsia = {"abu dhabi","amman","ankara","ashgabat","astana","baghdad","baku","bandar seri begawan","bangkok","beijing","beirut","bishkek","sri jayawardenepura kotte","dhaka","dili","doha","dushanbe","hanoi","islamabad","jakarta","jerusalem",
            "kabul","kathmandu","kuala lumpur","kuwait city","lhasa","male","manama","manila","moscow","muscat","naypyidaw","new delhi","phnom penh","pyongyang","riyadh","sana'a","seoul","singapore","tashkent","tbilisi","tehran","thimphu","tokyo",
            "ulaanbaatar","vientiane","yerevan","damascus","jerusalem (east)","maynila"
    };

    private static final String[] eastAsiaMonuments = {"the_great_wall", "the_taj_mahal", "mount_fuji", "angkor_wat", "mount_everest", "the_great_buddha_of_kamakura"};

    private static final String[] westAsia = {"abu dhabi","amman","ankara","ashgabat","astana","baghdad","baku","bandar seri begawan","bangkok","beijing","beirut","bishkek","sri jayawardenepura kotte","dhaka","dili","doha","dushanbe","hanoi","islamabad","jakarta","jerusalem",
            "kabul","kathmandu","kuala lumpur","kuwait city","lhasa","male","manama","manila","moscow","muscat","naypyidaw","new delhi","phnom penh","pyongyang","riyadh","sana'a","seoul","singapore","tashkent","tbilisi","tehran","thimphu","tokyo",
            "ulaanbaatar","vientiane","yerevan","damascus","jerusalem (east)","maynila"
    };

    private static final String[] westAsiaMonuments = {"burj_al_arab_hotel", "mecca", "al_aqsa_mosque", "petra"};

    private static final String[] australia = {"canberra","suva","tarawa","majuro","palikir","yaren district","wellington","ngerulmud","port moresby","apia"
            ,"honiara","nuku'alofa","funafuti","port vila"
    };

    private static final String[] australiaMonuments = {"uluru"};

    public static int getCorrectAnswerContinentResource(String city) {
        List<String> africa = Arrays.asList(Continents.africa);
        List<String> africaMonuments = Arrays.asList(Continents.africaMonuments);
        if(africa.contains(city.toLowerCase()) || africaMonuments.contains(city.toLowerCase())) {
            return R.drawable.africa_map;
        }
        List<String> northAmerica = Arrays.asList(Continents.northAmerica);
        List<String> northAmericaMonuments = Arrays.asList(Continents.northAmericaMonuments);
        if(northAmerica.contains(city.toLowerCase()) || northAmericaMonuments.contains(city.toLowerCase())) {
            return R.drawable.north_america_map;
        }
        List<String> southAmerica = Arrays.asList(Continents.southAmerica);
        List<String> southAmericaMonuments = Arrays.asList(Continents.southAmericaMonuments);
        if(southAmerica.contains(city.toLowerCase()) || southAmericaMonuments.contains(city.toLowerCase())) {
            return R.drawable.south_america_map;
        }
        List<String> europe = Arrays.asList(Continents.europe);
        List<String> europeMonuments = Arrays.asList(Continents.europeMonuments);
        if(europe.contains(city.toLowerCase()) || europeMonuments.contains(city.toLowerCase())) {
            return R.drawable.europe_map;
        }
        List<String> eastAsia = Arrays.asList(Continents.eastAsia);
        List<String> eastAsiaMonuments = Arrays.asList(Continents.eastAsiaMonuments);
        if(eastAsia.contains(city.toLowerCase()) || eastAsiaMonuments.contains(city.toLowerCase())) {
            return R.drawable.east_asia_map;
        }
        List<String> westAsia = Arrays.asList(Continents.westAsia);
        List<String> westAsiaMonuments = Arrays.asList(Continents.westAsiaMonuments);
        if(westAsia.contains(city.toLowerCase()) || westAsiaMonuments.contains(city.toLowerCase())) {
            return R.drawable.west_asia_map;
        }
        List<String> australia = Arrays.asList(Continents.australia);
        List<String> australiaMonuments = Arrays.asList(Continents.australiaMonuments);
        if(australia.contains(city.toLowerCase()) || australiaMonuments.contains(city.toLowerCase())) {
            return R.drawable.australia_map;
        }
        return 0;
    }


}
