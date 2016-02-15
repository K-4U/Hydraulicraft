package k4unl.minecraft.Hydraulicraft.lib;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.nbt.NBTTagCompound;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IPs {

    private Random rnd;
    private boolean isLoaded = false;
    private Map<Long, Location> registeredIps;


    public IPs() {

        registeredIps = new HashMap<Long, Location>();
        rnd = new Random(System.currentTimeMillis() / 1000);
    }

    public void readFromFile(File dir) {

        registeredIps.clear();
        if (dir != null) {
            Gson gson = new Gson();
            String p = dir.getAbsolutePath();
            p += "/portals.json";
            File f = new File(p);
            if (f.exists()) {
                f.delete();
            }
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

                Type myTypeMap = new TypeToken<Map<Long, Location>>() {
                }.getType();
                registeredIps = gson.fromJson(json, myTypeMap);
                if (registeredIps == null) {

                    registeredIps = new HashMap<Long, Location>();
                }

                //Log.info("Read from file: " + json);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        NBTTagCompound tCompound = new NBTTagCompound();
        isLoaded = true;
        int entries = tCompound.getInteger("entries");
        for (int i = 0; i < entries; i++) {
            NBTTagCompound entryCompound = tCompound.getCompoundTag("" + i);
            long key = entryCompound.getLong("key");
            Location value = new Location(entryCompound.getIntArray("location"));
            registeredIps.put(key, value);
        }
    }

    public void saveToFile(File dir) {

        if (dir != null) {
            Gson gson = new Gson();
            String json = gson.toJson(registeredIps);
            //Log.info("Saving: " + json);
            String p = dir.getAbsolutePath();
            p += "/portals.json";
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

    public boolean IPExists(long ip) {

        return registeredIps.containsKey(ip);
    }

    public void registerIP(long ip, Location loc) {

        registeredIps.put(ip, loc);
    }

    public void removeIP(long ip) {

        registeredIps.remove(ip);
    }

    public String generateNewRandomIP(int dimensionID) {

        String[] IP = {"10", (dimensionID + 2) + "", "0", "0"};
        String fullIP = "";
        boolean redo = true;
        while (redo) {
            IP[2] = rnd.nextInt(253) + 1 + "";
            IP[3] = rnd.nextInt(253) + 1 + "";

            fullIP = IP[0] + "." + IP[1] + "." + IP[2] + "." + IP[3];
            redo = IPExists(ipToLong(fullIP));
        }
        return fullIP;
    }

    public static long ipToLong(String IP) {

        long result = 0;
        String[] pieces = IP.split("\\.");

        for (int i = 3; i >= 0; i--) {
            result |= (Long.parseLong(pieces[3 - i]) << (i * 8));
        }

        return result;
    }

    public static String longToIp(long IP) {

        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            sb.insert(0, Long.toString(IP & 0xFF));
            if (i < 3) {
                sb.insert(0, '.');
            }

            IP >>= 8;
        }
        return sb.toString();
    }

    public Location getLocation(long linked) {

        return registeredIps.get(linked);
    }
}
