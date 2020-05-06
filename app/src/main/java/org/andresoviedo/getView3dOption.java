package org.andresoviedo;

public class getView3dOption {
    private static boolean view3D = false;
    public  boolean getView3d() {
        return view3D;
    }
    public static void setView3D(boolean choice) {
        getView3dOption.view3D = choice;
    }
}
