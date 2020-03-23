import org.apache.commons.io.IOUtils;
import org.apache.commons.io.IOUtils;
import java.io.IOException;


public class Main {

    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception{
        String output = (new Main()).readRawDataToString();
        System.out.println(output);

       JerkSONParser jerksonParser = new JerkSONParser();
        String report = jerksonParser.parse(output);
        System.out.println(report);



    }
}
