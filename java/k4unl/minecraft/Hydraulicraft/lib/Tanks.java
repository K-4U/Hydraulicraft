package k4unl.minecraft.Hydraulicraft.lib;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.util.BlockPos;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Tanks {

    class Tank {

        private Location location1;
        private Location location2;
        private Location valveLocation;

        public Tank(Location loc1, Location loc2, Location _valveLocation) {

            location1 = loc1;
            location2 = loc2;
            valveLocation = _valveLocation;
        }

        public boolean isLocationInTank(BlockPos pos) {

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            if (x >= location1.getX() && x <= location2.getX()) {
                if (y >= location1.getY() && y <= location2.getY()) {
                    if (z >= location1.getZ() && z <= location2.getZ()) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean isEqual(Location loc1, Location loc2) {

            return location1.equals(loc1) && location2.equals(loc2);
        }

        public Location getValveLocation() {

            return valveLocation;
        }
    }

    private boolean isLoaded = false;
    private List<Tank> registeredTanks;

    public Tanks() {

        registeredTanks = new ArrayList<Tank>();
    }


    public void addNewTank(Location loc1, Location loc2, Location valveLocation) {

        Tank toAdd = new Tank(loc1, loc2, valveLocation);
        registeredTanks.add(toAdd);
    }

    public void deleteTank(Location loc1, Location loc2) {

        for (Tank tank : registeredTanks) {
            if (tank.isEqual(loc1, loc2)) {
                registeredTanks.remove(tank);
                break;
            }
        }
    }

    public Location isLocationInTank(BlockPos pos) {

        for (Tank tank : registeredTanks) {
            if (tank.isLocationInTank(pos)) {
                return tank.getValveLocation();
            }
        }
        return null;
    }

    public void readFromFile(File dir) {

        registeredTanks.clear();
        if (dir != null) {
            Gson gson = new Gson();
            String p = dir.getAbsolutePath();
            p += "/tanks.json";
            File f = new File(p);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                FileInputStream ipStream = new FileInputStream(f);
                InputStreamReader reader = new InputStreamReader(ipStream);
                BufferedReader bReader = new BufferedReader(reader);
                String json = bReader.readLine();
                reader.close();
                ipStream.close();
                bReader.close();

                Type myTypeMap = new TypeToken<List<Tank>>() {
                }.getType();
                registeredTanks = gson.fromJson(json, myTypeMap);
                if (registeredTanks == null) {

                    registeredTanks = new ArrayList<Tank>();
                }

                //Log.info("Read from file: " + json);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void saveToFile(File dir) {

        if (dir != null) {
            Gson gson = new Gson();
            String json = gson.toJson(registeredTanks);
            //Log.info("Saving: " + json);
            String p = dir.getAbsolutePath();
            p += "/tanks.json";
            File f = new File(p);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                PrintWriter opStream = new PrintWriter(f);
                opStream.write(json);
                opStream.flush();
                opStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
