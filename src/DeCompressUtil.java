

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
/**
 * @author yy

 */
public class DeCompressUtil {

	/**
	 * 
	 * 解压zip33333333333333格缩包
	 * 
	 * 对应的是ant.jar
	 */

	private static void unzip(String sourceZip, String destDir)
			throws Exception {

		Project p = new Project();

		Expand e = new Expand();

		e.setProject(p);

		e.setSrc(new File(sourceZip));

		e.setOverwrite(false);

		e.setDest(new File(destDir));

		/*
		 * 
		 * ant下的zip工具默认压缩编码为UTF-8编码，
		 * 
		 * 而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
		 * 
		 * 所以解压缩时要制定编码格式
		 */

		e.setEncoding("gbk");

		e.execute();

	}
	public static boolean existZH(String str) {  
	    String regEx = "[\\u4e00-\\u9fa5]";  
	    Pattern p = Pattern.compile(regEx);  
	    Matcher m = p.matcher(str);  
	    while (m.find()) {  
	        return true;  
	    }  
	    return false;  
	} 
	/**
	 * 
	 * 解压rar格式压缩包。
	 * 
	 * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar
	 
	private static void unrar(String sourceRar, String destDir)
			throws Exception {

		Archive a = null;

		FileOutputStream fos = null;

		try {

			a = new Archive(new File(sourceRar));

			FileHeader fh = a.nextFileHeader();  

			while (fh != null) {

				if (!fh.isDirectory()) {

					// 1 根据不同的操作系统拿到相应的 destDirName 和 destFileName

					String compressFileName = fh.getFileNameW().trim();
					if(!existZH(compressFileName)){
						compressFileName=fh.getFileNameString().trim();
					}

					String destFileName = "";

					String destDirName = "";

					// 非windows系统  

					if (File.separator.equals("/")) {

						destFileName = destDir
								+ compressFileName.replaceAll("\\\\", "/");

						destDirName = destFileName.substring(0, destFileName
								.lastIndexOf("/"));

						// windows系统

					} else {

						destFileName = destDir
								+ compressFileName.replaceAll("/", "\\\\");

						destDirName = destFileName.substring(0, destFileName
								.lastIndexOf("\\"));

					}

					// 2创建文件夹

					File dir = new File(destDirName);

					if (!dir.exists() || !dir.isDirectory()) {

						dir.mkdirs();

					}

					// 3解压缩文件

					fos = new FileOutputStream(new File(destFileName));

					a.extractFile(fh, fos);

					fos.close();

					fos = null;

				}

				fh = a.nextFileHeader();

			}

			a.close();

			a = null;

		} finally {

			if (fos != null) {
				fos.close();
				fos = null;
			}
			if (a != null) {
				a.close();
				a = null;
			}

		}

	}
	
*/

	/**
	 * 
	 * 解压缩
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param destDir
	 *            目录
	 * @throws Exception
	 *             异常
	 */
	/**
	public static void deCompress(String sourceFile, String destDir)
			throws Exception {
		// 保证文件夹路径最后是"/"或者"\"
		char lastChar = destDir.charAt(destDir.length() - 1);
		if (lastChar != '/' && lastChar != '\\') {
			destDir += File.separator;
		}

		// 根据类型，进行相应的解压缩
		String type = sourceFile.substring(sourceFile.lastIndexOf(".") + 1);
		if (type.equals("zip")) {
			DeCompressUtil.unzip(sourceFile, destDir);
		} else if (type.equals("rar")) {
			DeCompressUtil.unrar(sourceFile, destDir);
		} else
			throw new Exception("只支持zip和rar格式的压缩包！");

	}
	
	*/

	/**
     * 压缩整个文件夹中的所有文件，生成指定名称的zip压缩包
     * @param filepath 文件所在目录
     * @param zippath 压缩后zip文件名称
     * @param dirFlag zip文件中第一层是否包含一级目录，true包含；false没有
     */
    public static void zipMultiFile(String filepath ,String zippath, boolean dirFlag) {
        try {
            File file = new File(filepath);// 要被压缩的文件夹
            File zipFile = new File(zippath);
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(File fileSec:files){
                    if(dirFlag){
                        recursionZip(zipOut, fileSec, file.getName() + File.separator);
                    }else{
                        recursionZip(zipOut, fileSec, "");
                    }
                }
            }
            zipOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    private static void recursionZip(ZipOutputStream zipOut, File file, String baseDir) throws Exception{
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File fileSec:files){
                recursionZip(zipOut, fileSec, baseDir + file.getName() + File.separator);
            }
        }else{
            byte[] buf = new byte[1024];
            InputStream input = new FileInputStream(file);
            zipOut.putNextEntry(new ZipEntry(baseDir + file.getName()));
            int len;
            while((len = input.read(buf)) != -1){
                zipOut.write(buf, 0, len);
            }
            input.close();
        }
    }
    
	public static void main(String[] args) {
		try {
			DeCompressUtil.unzip(("D:/开发工具/SplitFile158.zip"),"D:/开发工具1/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		DeCompressUtil.zipMultiFile("C:/Users/yy/Desktop/论文最终版", "D://zz.zip", true);
	}


}

