public class Main {
    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager(args[1],args[0],args[2],args[3]);
        manager.processCommands(args[2]);

    }
}