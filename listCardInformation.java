package fr.imag.javacard;

import javax.smartcardio.*;
import java.util.*;

public class Main {
    static class Util {
        static final String HEXES = "0123456789ABCDEF";

        public static String getHex(byte[] raw) {
            if (raw == null) {
                return null;
            }
            final StringBuilder hex = new StringBuilder(2 * raw.length);
            for (final byte b : raw) {
                hex.append(HEXES.charAt((b & 0xF0) >> 4))
                        .append(HEXES.charAt((b & 0x0F)));
            }
            return hex.toString();
        }
    }

    private static byte[] appletAID = {0x55, 0x6E, 0x69, 0x76, 0x2E, 0x55, 0x4A, 0x46};
    private final static byte INS_SELECT = (byte) 0xA4;
    private final static byte INS_GETDATA = (byte) 0xCA;

    static class Command {
        String description;

        Command(String description) {
            this.description = description;
        }
    }

    private static final Map<Integer, Command> COMMAND_MAP =
            Collections.unmodifiableMap(new HashMap<Integer, Command>() {{
                put(1, new Command("ID(?)"));
                put(2, new Command("Version(?)"));
                put(3, new Command("(?)"));
                put(4, new Command("INE"));
                put(5, new Command("Creation(?)"));
                put(6, new Command("Update(?)"));
            }});

    public static CardTerminal selectCardTerminal() throws Exception {
        Scanner in = new Scanner(System.in);
        TerminalFactory tf = TerminalFactory.getDefault();
        List<CardTerminal> ctlist = null;
        ctlist = tf.terminals().list();
        if (ctlist.size() == 0) {
            throw new Exception("No reader present");
        }
        if (ctlist.size() == 1) {
            return ctlist.get(0);
        }

        int index = 0;
        do {

            System.out.println("Readers:");
            System.out.println("============================================");
            for (CardTerminal ct : ctlist) {
                System.out.println(index++ + ": " + ct.getName());
            }
            System.out.print("Select a reader:");
            index = in.nextInt();
        } while (index < 0 || index >= ctlist.size());

        return ctlist.get(index);
    }

    public static void selectApplication(CardChannel cc) throws Exception {
        System.out.println("Selecting \"Univ.UJF\" application");

        CommandAPDU apdu = new CommandAPDU(
                0x00, INS_SELECT,
                0x4, 0x0,
                appletAID);

        ResponseAPDU rapdu = cc.transmit(apdu);
        if (rapdu.getSW() != 0x9000) {
            throw new Exception("Can't select \"Univ.UJF\" application: "
                    + Integer.toHexString(rapdu.getSW()));
        }

    }

    public static void main(String[] args) {
        try {
            CardTerminal ct = selectCardTerminal();
            Card c = ct.connect("T=0");
            ATR atr = c.getATR();
            System.out.println("ATR : " + Util.getHex(atr.getBytes()));
            CardChannel cc = c.getBasicChannel();

            selectApplication(cc);

            for (int id : COMMAND_MAP.keySet()) {
                Command cmd = COMMAND_MAP.get(id);
                CommandAPDU apdu = new CommandAPDU(
                        0x00, INS_GETDATA,
                        (byte) id, 0x00, new byte[]{0x01});

                ResponseAPDU rapdu = cc.transmit(apdu);
                if (rapdu.getSW() != 0x9000) {
                    throw new Exception("Can't get data (" + id + "): "
                            + Integer.toHexString(rapdu.getSW()));
                }
                System.out.println(cmd.description + ":"
                        + new String(rapdu.getData()));
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
