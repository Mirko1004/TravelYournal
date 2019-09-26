package zkezic.fesb.mimosproject.presentation.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static File renameFile(final Context context, final File fileToRename,
                                  final String newName) {
        File newFile = null;
        if(fileToRename != null) {
            File appDirectory = new File(Environment.getExternalStorageDirectory(), "Travel journal");
            appDirectory.mkdir();
            newFile = new File(appDirectory, newName);
            fileToRename.renameTo(newFile);
        }

        return newFile;
    }

    public static File createFile(final Context context, final String fileName, final Uri imageUri) {
        InputStream inputStream = null;
        File fileToCreate = null;

        try {
            File appDirectory = new File(Environment.getExternalStorageDirectory(), "Travel journal");
            appDirectory.mkdir();
            fileToCreate = new File(appDirectory, fileName);
            if(fileToCreate.exists()) fileToCreate.delete();
            
            inputStream = context.getContentResolver().openInputStream(imageUri);

            OutputStream output = new FileOutputStream(fileToCreate);
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    try {
                        output.write(buffer, 0, read);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileToCreate;
    }

    // validacija datuma
    public static boolean isThisDateValid(String dateToValidate, String dateFormat){

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {
            Date date = sdf.parse(dateToValidate);
        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
                "Travel journal");

        if (!mediaStorageDir.exists()){
            mediaStorageDir.mkdirs();
        }

        File outputFile = new File(mediaStorageDir,
                "temp_image.png");

        if(outputFile.exists()) outputFile.delete();

        return outputFile;
    }

    public static String getFileNameFromPath(File file) {
        String fileName = "";

        if(file != null) {
            fileName = String.format("Travel journal/%s", file.getName());
        }

        return fileName;
    }
}
