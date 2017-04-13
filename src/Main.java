import org.ini4j.*;
import org.ini4j.Profile.Section;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String filename = args.length > 0 ? args[0] : "1.ini";

        Config config = new Config();
        config.setFileEncoding(Charset.forName("utf-16le"));
        config.setStrictOperator(true);
        config.setEscape(false);
        config.setMultiOption(true);
        config.setMultiSection(true);

        Ini ini = new Wini();
        Ini ini2 = new Wini();
        Ini ini3 = new Wini();

        ini.setConfig(config);
        ini2.setConfig(config);
        ini3.setConfig(config);

        ini.load(new File(filename));

        for (String sectionName : ini.keySet()) {
            List<Section> sectionList = ini.getAll(sectionName);
            for (Section section : sectionList) {
                for (String optionName : section.keySet()) {
                    List<String> optionList = section.getAll(optionName);
                    for (String optionVaule : optionList) {
                        System.out.printf("%s %s %s\n", sectionName, optionName, optionVaule);
                    }
                }
            }

            Section section = ini.get(sectionName);
            ini2.add(sectionName.toLowerCase());
            Section section2 = ini2.get(sectionName.toLowerCase());
            for (String optionKey : section.keySet()) {
                section2.add(optionKey.toLowerCase(), section.get(optionKey));
            }

        }

        String fileNameBefore = "1";
        FileOutputStream fos = new FileOutputStream(fileNameBefore + filename);
        OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("utf-16le"));
        osw.write("\uFEFF");
        ini2.store(osw);


    }
}
