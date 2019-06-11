package com.orestis.velen.quiz.geography;

import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.QuestionPoolData;
import com.orestis.velen.quiz.repositories.DataRepository;
import com.orestis.velen.quiz.repositories.SampleSizer;

import static com.orestis.velen.quiz.questions.Difficulty.EASY;
import static com.orestis.velen.quiz.questions.Difficulty.HARD;
import static com.orestis.velen.quiz.questions.Difficulty.NORMAL;

public class GeographyDataRepository implements DataRepository{

    private String[] allEasyCountries = {"china","india","united_states","indonesia","brazil","pakistan","nigeria","bangladesh","russia","mexico","japan","ethiopia","philippines","vietnam","egypt","dr_congo","iran","germany","turkey","thailand",
            "united_kingdom","france","italy","tanzania","south_africa","myanmar","south_korea","colombia","kenya","spain","ukraine","argentina","sudan","uganda","algeria","iraq","poland","canada","morocco","afghanistan",
            "saudi_arabia","peru","venezuela","malaysia","uzbekistan","mozambique","nepal","ghana","yemen","angola","madagascar","north_korea","australia","cameroon","ivory_coast","niger","sri_lanka","romania","burkina_faso","syria"
    };
    private String[] allEasyCapitals = {"beijing","new Delhi","washington","jakarta","brasilia","islamabad","abuja","dhaka","moscow","mexico city","tokyo","addis Ababa","maynila","hanoi","cairo","kinshasa","tehran","berlin","ankara","bangkok",
            "london","paris","rome","dodoma","pretoria","naypyidaw","seoul","bogota","nairobi","madrid","kiev","buenos Aires","khartoum","kampala","algiers","baghdad","warsaw","ottawa","rabat","kabul",
            "riyadh","lima","caracas","kuala Lumpur","tashkent","maputo","kathmandu","accra","Sana'a","luanda","antananarivo","pyongyang","canberra","yaounde","yamoussoukro","niamey","sri Jayawardenepura Kotte","bucharest","Ouagadougou","damascus"
    };

    private String[] allMediumCountries = {"mali","chile","malawi","kazakhstan","zambia","netherlands","guatemala","ecuador","zimbabwe","cambodia","senegal","chad","guinea","south_sudan","rwanda","burundi","tunisia","benin","belgium","somalia",
            "cuba","bolivia","haiti","greece","dominican_republic","czech_republic","portugal","azerbaijan","sweden","hungary","belarus","united_arab_emirates","tajikistan","serbia","austria","switzerland","israel","honduras","papua_new_guinea","jordan",
            "togo","bulgaria","laos","paraguay","sierra_leone","libya","nicaragua","el_salvador","kyrgyzstan","lebanon","singapore","denmark","finland","turkmenistan","eritrea","slovakia","norway","central_african_republic","palestine","costa_rica"
    };
    private String[] allMediumCapitals = {"bamako","santiago","lilongwe","astana","lusaka","amsterdam","guatemala City","quito","harare","phnom Penh","dakar","n'Djamena","conakry","juba","kigali","bujumbura","tunis","porto-Novo","brussels","mogadishu",
            "havana","la Paz","port-au-Prince","athens","santo Domingo","prague","lisbon","baku","stockholm","budapest","minsk","abu Dhabi","dushanbe","belgrade","vienna","bern","jerusalem","tegucigalpa","port Moresby","amman",
            "lome","sofia","vientiane","asunción","freetown","tripoli","managua","san Salvador","bishkek","beirut","singapore","copenhagen","helsinki","ashgabat","asmara","bratislava","oslo","bangui","jerusalem (East)","san Jose"
    };

    private String[] allHardCountries = {"congo","ireland","oman","liberia","new_zealand","mauritania","croatia","kuwait","moldova","panama","georgia","bosnia_herzegovina","uruguay","mongolia","armenia","albania","lithuania","jamaica","namibia","botswana",
            "qatar","lesotho","gambia","slovenia","latvia","guinea_bissau","gabon","bahrain","trinidad_and_tobago","swaziland","estonia","mauritius","cyprus","djibouti","fiji","equatorial_guinea","comoros","bhutan",
            "guyana","montenegro","solomon_islands","luxembourg","suriname","cabo_verde","brunei","malta","bahamas","maldives","belize","iceland","barbados","vanuatu","sao_tome_and_principe","samoa","saint_lucia","kiribati","saint_vincent_and_grenadines","grenada",
            "tonga","micronesia","seychelles","antigua_and_barbuda","dominica","andorra","saint_kitts_and_nevis","marshall_islands","liechtenstein","monaco","san_marino","palau","nauru","tuvalu","vatican"
    };
    private String[] allHardCapitals = {"brazzaville","dublin","muscat","monrovia","wellington","nouakchott","zagreb","kuwait City","chisinau","panama City","tbilisi","sarajevo","montevideo","ulaanbaatar","yerevan","tirana","vilnius","kingston","windhoek","gaborone",
            "doha","maseru","banjul","ljubljana","riga","bissau","libreville","manama","port of Spain","mbabane","tallinn","port Louis","nicosia","djibouti","suva","oyala","moroni","thimphu",
            "georgetown","podgorica","honiara","luxembourg (city)","paramaribo","praia","bandar Seri Begawan","valletta","nassau","male","belmopan","reykjavik","bridgetown","port Vila","São Tomé","apia","castries","tarawa","kingstown","saint George's",
            "nuku'alofa","palikir","victoria","saint John's","roseau","andorra la Vella","basseterre","majuro","vaduz","monaco","san Marino","ngerulmud","yaren District","funafuti","vatican City"

    };

    private String[] allMonuments = {"the_great_sphynx_giza", "giza_pyramids", "victoria_falls", "cape_of_good_hope", "table_mountain",
            "statue_of_liberty", "capitol_hill", "niagara_falls", "mount_rushmore", "grand_canyon", "chichen_itza", "inukshuk",
            "machu_picchu", "christ_the_redeemer", "easter_island", "nevado_mismi",
            "eiffel_tower", "st_basil_catherdal", "blue_domed_hurch_santorini", "the_little_mermaid", "neptune_and_the_palace_of_versailles",
            "windmills_of_kinderdijk", "big_ben", "tower_of_pisa", "lascaux", "loch_ness", "mont_st_michel", "bran_castle", "agia_sophia", "brandenburg_gate",
            "acropolis", "sagrada_familia", "neuschwanstein", "stonehenge", "manneken_pis", "st_peter_cathedral", "trevi_fountain", "auschwitz", "north_cape",
            "the_great_wall", "the_taj_mahal", "mount_fuji", "angkor_wat", "mount_everest", "the_great_buddha_of_kamakura",
            "burj_al_arab_hotel", "mecca", "al_aqsa_mosque", "petra",
            "uluru"};

    private String[] empty = {"", "", ""};

    public QuestionPoolData getDataFor(Difficulty difficulty, GameType gameType, int sampleSize) {
        if(difficulty == EASY) {
            SampleSizer sampleSizer = new SampleSizer(allEasyCountries, allEasyCapitals, sampleSize);
            switch (gameType) {
                case TYPE_A:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getFirst());
                case TYPE_B:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getSecond());
                case TYPE_C:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getFirst());
                case TYPE_D:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getSecond());
                case TYPE_E:
                    SampleSizer landmarkSampleSizer = new SampleSizer(allMonuments, empty, sampleSize);
                    return new QuestionPoolData(landmarkSampleSizer.getFirst(), landmarkSampleSizer.getFirst());
            }
        }
        if(difficulty == NORMAL) {
            SampleSizer sampleSizer = new SampleSizer(allMonuments, allMediumCapitals, sampleSize);
            switch (gameType) {
                case TYPE_A:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getFirst());
                case TYPE_B:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getSecond());
                case TYPE_C:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getFirst());
                case TYPE_D:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getSecond());
            }
        }
        if(difficulty == HARD) {
            SampleSizer sampleSizer = new SampleSizer(allHardCountries, allHardCapitals, sampleSize);
            switch (gameType) {
                case TYPE_A:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getFirst());
                case TYPE_B:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getSecond());
                case TYPE_C:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getFirst());
                case TYPE_D:
                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getSecond());
            }
        }
        return null;
    }
}
