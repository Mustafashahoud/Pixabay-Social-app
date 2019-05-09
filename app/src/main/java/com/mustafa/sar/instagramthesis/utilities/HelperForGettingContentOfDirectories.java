package com.mustafa.sar.instagramthesis.utilities;

import java.io.File;
import java.util.ArrayList;

public class HelperForGettingContentOfDirectories {

    /**
     * Get directory paths array list.
     * @param pathName the path name
     * @return All the paths of directories inside the directory {all **directories** contained inside the directory}
     */
    public static ArrayList<String> getDirectoryPaths(String pathName){
        ArrayList<String> pathDirArray = new ArrayList<>();
        File file = new File(pathName); // We've got the file now
        File[] listFiles = file.listFiles(); // Here We've got all files or directories inside the Directory,
        //  but to be more specific an array of abstract pathnames
        for (int i = 0; i < listFiles.length ; i++) {
            if (listFiles[i].isDirectory()){
                //pathDirArray.add(listFiles[i].getAbsolutePath());
                pathDirArray.add(listFiles[i].getAbsolutePath());

            }
        }
        return pathDirArray;
    }

    /**
     * Get directory paths array list.
     * @param pathName the path name
     * @return All the paths of directories inside the directory {all **Files** contained inside the directory}
     */
    public static ArrayList<String> getFilesPaths(String pathName){
        ArrayList<String> pathFileArray = new ArrayList<>();
        File file = new File(pathName); // We've got the file now
        File[] listFiles = file.listFiles(); // Here We've got all files or directories inside the Directory,
        //  but to be more specific an array of abstract pathnames
        for (int i = 0; i < listFiles.length ; i++) {
            if (listFiles[i].isFile()){
                //pathFileArray.add(listFiles[i].getAbsolutePath());
                pathFileArray.add(listFiles[i].getAbsolutePath());
            }
        }
        return pathFileArray;

    }
    /**
     * Get directory paths array list.
     * @param pathName the path name
     * @return All the paths of directories inside the directory {all **directories** contained inside the directory}
     */
    public static ArrayList<String> getDirectoryNames(String pathName){
        ArrayList<String> pathDirArray = new ArrayList<>();
        File file = new File(pathName); // We've got the file now
        File[] listFiles = file.listFiles(); // Here We've got all files or directories inside the Directory,
        //  but to be more specific an array of abstract pathnames
        for (int i = 0; i < listFiles.length ; i++) {
            if (listFiles[i].isDirectory()){
                //pathDirArray.add(listFiles[i].getAbsolutePath());
                pathDirArray.add(listFiles[i].getName());

            }
        }
        return pathDirArray;

    }

    /**
     * Get directory paths array list.
     * @param pathName the path name
     * @return All the paths of directories inside the directory {all **Files** contained inside the directory}
     */
    public static ArrayList<String> getFilesNames(String pathName){

        ArrayList<String> pathFileArray = new ArrayList<>();
        File file = new File(pathName); // We've got the file now
        File[] listFiles = file.listFiles(); // Here We've got all files or directories inside the Directory,
        //  but to be more specific an array of abstract pathnames
        for (int i = 0; i < listFiles.length ; i++) {
            if (listFiles[i].isFile()){
                //pathFileArray.add(listFiles[i].getAbsolutePath());
                pathFileArray.add(listFiles[i].getName());
            }
        }
        return pathFileArray;
    }
}
