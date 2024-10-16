package JavaDataBase;

import java.util.HashSet;
import java.util.Random;

import JavaDataBase.Exceptions.AccountCreationException;
import JavaDataBase.Exceptions.AccountNotFoundException;
import JavaDataBase.Exceptions.DataBaseConnectionException;

public class GuideGenerator {
    private final ManagerLayer database;
    private final Random random = new Random();

    /**
     * Constructs a GuideGenerator with the specified database manager.
     * The purpose of the guide generator is to populate the database
     * with a set amount of pre-defined guides that explains common
     * problems with electronic devices, and how to solve them on a high level.
     * Difficulty levels are randomized.
     * 
     * @param database the ManagerLayer instance for database operations
     */
    public GuideGenerator(ManagerLayer database) {
        this.database = database;
    }

    /**
     * Generates hardcoded test guides and adds them to the database if they don't
     * already exist. It will not add the same guides multiple times.
     */
    public void generateGuides() {
        // Collect existing guide titles to avoid duplicates
        HashSet<String> existingTitles = new HashSet<>();
        try {
            for (Guide guide : database.getAllGuides()) {
                existingTitles.add(guide.getTitle());
            }
        } catch (DataBaseConnectionException e) {
            System.out.println(e);
            return; // Exit if we cannot retrieve existing guides
        }

        // Register a user to "create" the guides and get his Account.
        try {
            database.createAccount("RepairGuru", "VerySecurePassW");
        } catch (AccountCreationException e) {
            // Already exists, ignore creation.
            // Exceptions here are expected if server is restarted, ignore.
        }

        // Generate guides only if they don't already exist
        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            if (!existingTitles.contains(title)) {
                String content = generateGuideContent(i); // Pass index to get steps
                int difficulty = random.nextInt(5) + 1; // Random difficulty level from 1 to 5

                Account defaultAccount = null;
                try {
                    // Should always pass, since we create it above if it doesn't exist yet.
                    defaultAccount = database.getAccount("RepairGuru");
                } catch (AccountNotFoundException e) {
                    System.out.println(e);
                }

                // Create the guide in the database
                try {
                    database.createGuide(title, content, defaultAccount, difficulty);
                } catch (DataBaseConnectionException e) {
                    System.out.println(e);
                }
                System.out.println("Guide created: " + title);
            }
        }
    }

    /**
     * Generates the content for a guide with steps to the guide's
     * topic.
     * 
     * @param guideIndex the index of the guide to generate content for
     * @return generated guide content as a String
     */
    private String generateGuideContent(int guideIndex) {
        StringBuilder contentBuilder = new StringBuilder();
        // Create a generic guide intro
        contentBuilder.append("<h2>Introduction</h2>\n");
        contentBuilder.append("This guide will help you to repair or troubleshoot your electronic device.\n" +
                "The goal of this guide is to help you fix the issue with your electronic device instead of having you discard it.\n");

        // Create a set amount of predefined "Steps" as headers, to structure the
        // guides.
        int numberOfSteps = stepContent[guideIndex].length; // Get the number of steps
        for (int step = 0; step < numberOfSteps; step++) {
            contentBuilder.append("<h2>Step ").append(step + 1).append("</h2>\n");
            contentBuilder.append(stepContent[guideIndex][step]).append("\n");
        }

        return contentBuilder.toString();
    }

    // ------------------------------//
    // ----- Some Repair Guides -----//
    // ------------------------------//

    // Hardcoded repair guides for electronics
    // to make the database a bit more saturated
    private static final String[] titles = {
            "How to Repair a Smartphone Screen",
            "Fixing a Laptop Battery",
            "Replacing a Power Supply in a Desktop PC",
            "How to Clean a Circuit Board",
            "Repairing a Broken Charger",
            "Troubleshooting Wi-Fi Connectivity Issues",
            "Replacing Your Laptop's Faulty RAM",
            "How to Replace a Hard Drive",
            "Cleaning Your Printer's Printhead",
            "How to Fix Overheating Issues in Laptops",
            "Fixing a Smartwatch Display",
            "Repairing a Broken USB Port",
            "How to Fix Dead Pixels on a Monitor"
    };

    private static final String[][] stepContent = {
        { // How to Repair a Smartphone Screen
                "Begin by powering off the smartphone entirely. Remove the SIM card tray\n"
                + "and any external accessories. Having the phone powered on during this\n"
                + "repair can lead to short circuits.",
                "To soften the adhesive that holds the screen in place, use a heat gun,\n"
                + "warming the edges of the screen gently. Avoid overheating to prevent\n"
                + "internal component damage.",
                "Use a suction cup to lift the screen just enough to create a gap.\n"
                + "Carefully insert a plastic prying tool and work your way around the\n"
                + "edges to release the screen from the phone's frame.",
                "Once the screen is loose, disconnect the battery and display connector\n"
                + "using a Phillips screwdriver. This will prevent any electrical issues\n"
                + "while installing the new screen.",
                "Align the new screen with the phone frame, ensuring all connectors are\n"
                + "securely attached. Reassemble the device and press the screen firmly\n"
                + "back into place to ensure a secure fit.",
                "Finally, power the phone on and test the new screen for functionality,\n"
                + "including touch sensitivity and display quality."
        },
        { // Fixing a Laptop Battery
                "First, check if your laptop is charging. If not, inspect the power\n"
                + "adapter and the charging port. Sometimes, the issue might lie in\n"
                + "these areas rather than the battery.",
                "Turn off the laptop and unplug it. Remove the battery, either by\n"
                + "releasing the latch or unscrewing it, depending on your laptop model.",
                "Use a multimeter to test the battery's voltage. Compare the reading\n"
                + "with the recommended specifications to determine if the battery needs\n"
                + "replacing.",
                "If the battery is swollen or failing to hold a charge, install a\n"
                + "replacement. Make sure the new battery is compatible with your laptop\n"
                + "model.",
                "After replacing the battery, reassemble the laptop and charge it\n"
                + "fully. Turn it on to check if the laptop recognizes the new battery\n"
                + "and is functioning properly."
        },
        { // Replacing a Power Supply in a Desktop PC
                "Unplug your desktop and turn off the power switch on the back of the\n"
                + "power supply. Remove the side panel of the case to access the internal\n"
                + "components.",
                "Disconnect all power cables connected to the motherboard, hard drives,\n"
                + "and other internal components. Take note of where each cable was\n"
                + "connected for easier reassembly.",
                "Unscrew the power supply from the case, and carefully remove it. Make\n"
                + "sure to handle the power supply gently, as some residual power might\n"
                + "still be inside.",
                "Install the new power supply, screwing it securely into place.\n"
                + "Reconnect all the cables to their appropriate places, ensuring\n"
                + "everything is snug and properly aligned.",
                "Once reassembled, plug the computer back in, flip the power switch,\n"
                + "and turn the desktop on. Check for signs that the new power supply\n"
                + "is functioning, like the fans spinning and the system booting properly."
        },
        { // How to Clean a Circuit Board
                "Ensure the circuit board is disconnected from any power sources to\n"
                + "avoid electrical damage during the cleaning process.",
                "Use a soft brush to gently remove dust and debris from the surface of\n"
                + "the circuit board. Be cautious when cleaning around small, delicate\n"
                + "components.",
                "Prepare a cleaning solution using isopropyl alcohol and a lint-free\n"
                + "cloth. Dampen the cloth slightly and wipe down the circuit board,\n"
                + "focusing on areas with visible grime or dirt.",
                "Let the board dry completely before reconnecting it to power or\n"
                + "reassembling it. Any moisture left on the board can cause shorts\n"
                + "when powered up again."
        },
        { // Repairing a Broken Charger
                "Inspect the charger for any visible damage, like frayed cables or\n"
                + "bent connectors. If the damage is obvious, you'll know where to\n"
                + "focus your repair.",
                "Open the charger casing using a screwdriver. Be careful as chargers\n"
                + "contain small, delicate components that can be easily damaged.",
                "Replace any visibly damaged components, like the charging cable or\n"
                + "internal wiring. Ensure all connections are soldered securely and the\n"
                + "parts fit back into the casing properly.",
                "Reassemble the charger and test it with the device to ensure it\n"
                + "functions correctly. If the charger still doesn't work, the issue\n"
                + "might be more complex, requiring a replacement."
        },
        { // Troubleshooting Wi-Fi Connectivity Issues
                "Restart your router and check if the Wi-Fi connection is restored.\n"
                + "Sometimes, simply rebooting the router can fix most common issues.",
                "Make sure that airplane mode is not enabled on your device, as this\n"
                + "will disable all wireless communication.",
                "Forget the Wi-Fi network on your device and reconnect. This refreshes\n"
                + "the connection and can solve configuration conflicts.",
                "Check if your device's network drivers are up to date. Updating the\n"
                + "drivers can resolve software issues causing connectivity problems.",
                "If the issue persists, contact your Internet Service Provider (ISP) to\n"
                + "check if there are outages or issues from their end."
        },
        { // Replacing Your Laptop's Faulty RAM
                "Turn off the laptop completely and unplug it. Open the back panel to\n"
                + "access the RAM slots. You may need a small screwdriver to remove the\n"
                + "screws holding the panel in place.",
                "Carefully release the old RAM sticks by pushing the clips on each side\n"
                + "outward. The RAM should pop out of the slot easily.",
                "Insert the new RAM sticks by aligning them with the slots and pressing\n"
                + "them down until the clips snap back into place. Ensure the RAM is\n"
                + "firmly seated.",
                "Reassemble the laptop and power it on. Go to the system settings to\n"
                + "verify that the new RAM has been installed correctly and is recognized\n"
                + "by the system."
        },
        { // How to Replace a Hard Drive
                "Back up all important data from the old hard drive before you begin.\n"
                + "Data loss during replacement is a common risk, so prepare accordingly.",
                "Power off the computer, unplug it, and remove the case panel to access\n"
                + "the internal components.",
                "Locate the hard drive bay and disconnect the SATA and power cables\n"
                + "connected to the old hard drive.",
                "Install the new hard drive in the appropriate bay and secure it with\n"
                + "screws. Reconnect the SATA and power cables.",
                "Reassemble the computer and power it on. Install the operating system\n"
                + "or restore your backup onto the new hard drive."
        },
        { // Cleaning Your Printer's Printhead
                "From the printer's control panel, initiate the printhead cleaning utility.\n"
                + "This helps remove minor blockages from the nozzles automatically.",
                "Open the printer cover and remove the ink cartridges. Set them aside on\n"
                + "a clean surface to avoid spilling ink.",
                "Use a damp, lint-free cloth to clean the printhead gently, focusing on\n"
                + "areas with dried ink or dust.",
                "After cleaning, run the printer's self-cleaning utility again from the\n"
                + "software to ensure no blockages remain.",
                "Reinsert the ink cartridges and print a test page to check for any\n"
                + "remaining issues. If print quality remains poor, consider replacing\n"
                + "the printhead."
        },
        { // How to Fix Overheating Issues in Laptops
                "Ensure that your laptop is on a hard, flat surface that allows airflow\n"
                + "to the vents. Soft surfaces can block the airflow and lead to\n"
                + "overheating.",
                "Use compressed air to clean out the laptop's vents. Dust buildup inside\n"
                + "the laptop often causes airflow issues, which leads to overheating.",
                "Check if the cooling fan is functioning properly. If it's faulty,\n"
                + "replacing the fan can dramatically improve the laptop's cooling\n"
                + "performance.",
                "If the laptop continues to overheat, consider reapplying thermal paste\n"
                + "to the CPU. This helps improve heat transfer between the processor\n"
                + "and the cooling system.",
                "Using a cooling pad while using the laptop can also help maintain\n"
                + "lower temperatures, especially during intensive tasks like gaming or\n"
                + "video editing."
        },
        { // Fixing a Smartwatch Display
                "Turn off your smartwatch to avoid any short circuits or damage to\n"
                + "sensitive components.",
                "Use a specialized tool or a heat gun to soften the adhesive holding\n"
                + "the display in place. Be gentle to avoid damaging the display or\n"
                + "internal components.",
                "Carefully pry off the damaged screen using a plastic tool, starting\n"
                + "from one corner and working your way around the edges.",
                "Disconnect any internal display connectors using a small screwdriver,\n"
                + "and replace the damaged display with a new one.",
                "Reassemble the smartwatch, ensuring all connectors are securely\n"
                + "attached, and power it on to test the new display."
        },
        { // Repairing a Broken USB Port
                "Turn off your device and unplug it from any power source to avoid\n"
                + "electrical damage.",
                "Use a magnifying glass to inspect the damaged USB port for any bent\n"
                + "or broken connectors inside.",
                "Carefully use tweezers to realign bent connectors, ensuring that they\n"
                + "don't touch each other or the port housing.",
                "If the USB port is broken beyond repair, desolder the damaged port\n"
                + "and replace it with a new one using a soldering iron.",
                "After the replacement, test the USB port by connecting a device to\n"
                + "ensure proper functionality."
        },
        { // How to Fix Dead Pixels on a Monitor
                "Turn off your monitor and check for dead pixels by using an online\n"
                + "dead pixel detection tool.",
                "Gently apply pressure to the affected area using a soft cloth, which\n"
                + "may help revive stuck pixels.",
                "Use software designed to flash colors rapidly on the screen, which\n"
                + "can sometimes help fix dead or stuck pixels.",
                "If the pixels remain dead, you may need to contact the manufacturer\n"
                + "for a warranty replacement or consider replacing the screen."
        }
};


}
