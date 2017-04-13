import org.ini4j.*;
import org.ini4j.Profile.Section;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String iniFileName = args.length > 0 ? args[0] : "1.ini";
        System.out.printf("ini file name: %s\n", iniFileName);

        FileInputStream fis = new FileInputStream(iniFileName);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("utf-16le"));
        BufferedReader br = new BufferedReader(isr);

        String line;
        int i = 1;
        while ((line = br.readLine()) != null) {
            System.out.printf("%3d %s\n", i, line);
            i++;
        }

        FileOutputStream fos = new FileOutputStream("1" + iniFileName);
        OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("utf-16le"));
        osw.write("\uFEFF");

        osw.close();
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
