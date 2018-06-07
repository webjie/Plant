package person.jack.plant.utils;

/**
 * Created by kakee on 2018/6/7.
 */

public class UrlMethodUtil {

    public static byte[] local2byte(String url)throws Exception{  //由本地路径得到byte
        byte [] imageData = FileUtil.readFileByBytes(url);
        return imageData;
    }
    public static byte[] url2byte(String url)throws Exception {  //由url得到byte
        byte [] imageData =  IoUtil.getImageFromNetByUrl(url);
        return imageData;
    }
}
