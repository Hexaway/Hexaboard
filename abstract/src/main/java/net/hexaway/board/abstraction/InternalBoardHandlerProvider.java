package net.hexaway.board.abstraction;

import net.hexaway.board.abstraction.util.Version;

public final class InternalBoardHandlerProvider {

    private static final Object LOCK = new Object();
    private static volatile InternalBoardHandler OF_VERSION = null;

    private InternalBoardHandlerProvider() {
        throw new UnsupportedOperationException("cannot instantiate this class");
    }

    @SuppressWarnings("unchecked")
    public static InternalBoardHandler get() {
        if (OF_VERSION == null) {
            synchronized (LOCK) {
                if (OF_VERSION == null) {
                    String versionsPackage = "net.hexaway.board.version";
                    String className = Version.PACKAGE_NAME + InternalBoardHandler.class.getSimpleName();

                    try {
                        Class<InternalBoardHandler> implClass = (Class<InternalBoardHandler>) Class.forName(versionsPackage + "." + className);

                        return OF_VERSION = implClass.newInstance();
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException("Unable to find impl class of board handler for version: " + Version.PACKAGE_NAME, e);
                    }
                }
            }
        }

        return OF_VERSION;
    }
}