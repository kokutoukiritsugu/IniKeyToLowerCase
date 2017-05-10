import org.ini4j.*;
import org.ini4j.Profile.Section;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        String iniFileName = args.length > 0 ? args[0] : "1.ini";
        System.out.printf("ini file name: %s\n", iniFileName);

        FileInputStream fis = new FileInputStream(iniFileName);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("utf-16le"));
        isr.skip(1);
        BufferedReader br = new BufferedReader(isr);

        FileOutputStream fos = new FileOutputStream("" + iniFileName + "1");
        OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("utf-16le"));
        osw.write("\uFEFF");

        String line;
        while ((line = br.readLine()) != null) {
            boolean section_line = Pattern.matches("^\\s*\\[.*\\]\\s*$", line);
            boolean blank_line = Pattern.matches("^\\s*$", line);
            boolean comment_line = Pattern.matches("^[;].*\\s*$", line);
            boolean kv_line = Pattern.matches("^.*=.*$", line);

            if (comment_line) {
                osw.write(line);
                osw.write("\n");
                continue;
            }

            if (blank_line) {
                osw.write(line);
                osw.write("\n");
                continue;
            }

            if (section_line) {
                osw.write(line.toLowerCase());
                osw.write("\n");
                continue;
            }

            if (kv_line) {
                int keySize = line.indexOf("=");
                osw.write(line.substring(0, keySize).toLowerCase());
                osw.write(line.substring(keySize, line.length()));
                osw.write("\n");
                continue;
            }

            osw.write(line);
        }

        osw.close();

        isr.close();

        File oldF = new File(iniFileName);
        oldF.delete();

        File newF = new File(iniFileName + "1");
        newF.renameTo(oldF);
    }

    public static void old(String[] args) throws IOException {
        String iniFileName = args.length > 0 ? args[0] : "1.ini";
        System.out.printf("ini file name: %s\n", iniFileName);

        Config config = new Config();
        config.setFileEncoding(Charset.forName("utf-16le"));
        config.setStrictOperator(true);
        config.setEscape(false);
        config.setMultiOption(true);
        config.setMultiSection(true);

        Ini iniInput = new Wini();
        Ini iniOutput = new Wini();

        iniInput.setConfig(config);
        iniOutput.setConfig(config);

        iniInput.load(new File(iniFileName));

        for (String sectionName : iniInput.keySet()) {
            List<Section> sectionList = iniInput.getAll(sectionName);
            for (Section section : sectionList) {
                for (String optionName : section.keySet()) {
                    List<String> optionList = section.getAll(optionName);
                    for (String optionVaule : optionList) {
                        System.out.printf("  %s %s %s\n", sectionName, optionName, optionVaule);
                        iniOutput.add(sectionName.toLowerCase(), optionName.toLowerCase(), optionVaule);
                    }
                }
            }
        }

        FileOutputStream fos = new FileOutputStream(iniFileName);
        OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("utf-16le"));
        osw.write("\uFEFF");

        iniOutput.store(osw);

        osw.close();
    }
}
