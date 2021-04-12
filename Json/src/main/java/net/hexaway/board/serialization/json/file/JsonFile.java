package net.hexaway.board.serialization.json.file;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import java.io.*;

public final class JsonFile {

    private final File file;

    public JsonFile(File folder, String filename) {
        this(new File(folder, filename + ((filename.endsWith(".json")) ? "" : ".json")));
    }

    public JsonFile(File file) {
        Validate.notNull(file);

        this.file = file;
        createFile();
    }

    private void createFile() {
        if (file.exists())
            return;

        try {
            file.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void writeJson(String json) {
        if (!file.exists())
            createFile();

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public String getJson() throws IOException {
        if (!file.exists()) return "{}";

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        fileReader.close();
        bufferedReader.close();

        return stringBuilder.toString();
    }
}