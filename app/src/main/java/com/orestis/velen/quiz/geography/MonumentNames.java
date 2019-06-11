package com.orestis.velen.quiz.geography;

import java.util.HashMap;
import java.util.Map;

public class MonumentNames {

    private final Map<String, String> monuments = new HashMap<>();

    public MonumentNames() {
        //WestAsia
        monuments.put("burj_al_arab_hotel", "The Burj al Arab Hotel");
        monuments.put("mecca", "Mecca");
        monuments.put("al_aqsa_mosque", "Al Aqsa Mosque");
        monuments.put("petra", "Petra");

        //Europe
        monuments.put("eiffel_tower", "The Eiffel Tower");
        monuments.put("st_basil_catherdal", "St. Basil’s Cathedral");
        monuments.put("blue_domed_hurch_santorini", "Blue Domed Church");
        monuments.put("the_little_mermaid", "The Little Mermaid");
        monuments.put("neptune_and_the_palace_of_versailles", " Neptune and the Palace of Versailles");
        monuments.put("windmills_of_kinderdijk", "Windmills at Kinderdijk");
        monuments.put("big_ben", "Big Ben");
        monuments.put("tower_of_pisa", "Tower of Pisa");
        monuments.put("lascaux", "Lascaux");
        monuments.put("loch_ness", "Loch Ness");
        monuments.put("mont_st_michel", "Mont St. Michel");
        monuments.put("bran_castle", " Bran Castle");
        monuments.put("agia_sophia", "Agia Sophia");
        monuments.put("brandenburg_gate", "Brandenburg Gate");
        monuments.put("acropolis", "Acropolis");
        monuments.put("sagrada_familia", "Sagrada Familia");
        monuments.put("neuschwanstein", "Neuschwanstein Castle");
        monuments.put("stonehenge", "Stonehenge");
        monuments.put("manneken_pis", "Manneken Pis");
        monuments.put("st_peter_cathedral", "St. Peter's Basilica");
        monuments.put("trevi_fountain", "Trevi Fountain");
        monuments.put("auschwitz", "Auschwitz");
        monuments.put("north_cape", "North Cape");

        //Africa
        monuments.put("the_great_sphynx_giza", "The Great Sphinx");
        monuments.put("giza_pyramids", "The Pyramids");
        monuments.put("victoria_falls", "Victoria Falls");
        monuments.put("cape_of_good_hope", "Cape of Good Hope");
        monuments.put("table_mountain", "Table Mountain");

        //NorthAmerica
        monuments.put("statue_of_liberty", "The Statue of Liberty");
        monuments.put("capitol_hill", "Capitol Hill");
        monuments.put("niagara_falls", "Niagara Falls");
        monuments.put("mount_rushmore", "Mount Rushmore");
        monuments.put("grand_canyon", "The Grand Canyon");
        monuments.put("chichen_itza", "Chichen Itza");
        monuments.put("inukshuk", "Inukshuk");

        //EastAsia
        monuments.put("the_great_wall", "The Great Wall");
        monuments.put("the_taj_mahal", "The Taj Mahal");
        monuments.put("mount_fuji", "Mount Fuji");
        monuments.put("angkor_wat", "Angkor Wat");
        monuments.put("mount_everest", "Mount Everest");
        monuments.put("the_great_buddha_of_kamakura", "The Great Buddha of Kamakura");

        //SouthAmerica
        monuments.put("machu_picchu", "Machu Picchu");
        monuments.put("christ_the_redeemer", "Christ the Redeemer");
        monuments.put("easter_island", "Easter Island");
        monuments.put("nevado_mismi", "Nevado Mismi");

        //Australia
        monuments.put("uluru", "");
    }

    public String getMonumentNameFromCode(String monumentCode) {
        return monuments.get(monumentCode);
    }
}
