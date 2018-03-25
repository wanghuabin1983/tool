

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
	 * ��ѹzip33333333333333������
	 * 
	 * ��Ӧ����ant.jar
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
		 * ant�µ�zip����Ĭ��ѹ������ΪUTF-8���룬
		 * 
		 * ��winRAR���ѹ�����õ�windowsĬ�ϵ�GBK����GB2312����
		 * 
		 * ���Խ�ѹ��ʱҪ�ƶ������ʽ
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
	 * ��ѹrar��ʽѹ������
	 * 
	 * ��Ӧ����java-unrar-0.3.jar������java-unrar-0.3.jar�ֻ��õ�commons-logging-1.1.1.jar
	 
	private static void unrar(String sourceRar, String destDir)
			throws Exception {

		Archive a = null;

		FileOutputStream fos = null;

		try {

			a = new Archive(new File(sourceRar));

			FileHeader fh = a.nextFileHeader();  

			while (fh != null) {

				if (!fh.isDirectory()) {

					// 1 ���ݲ�ͬ�Ĳ���ϵͳ�õ���Ӧ�� destDirName �� destFileName

					String compressFileName = fh.getFileNameW().trim();
					if(!existZH(compressFileName)){
						compressFileName=fh.getFileNameString().trim();
					}

					String destFileName = "";

					String destDirName = "";

					// ��windowsϵͳ  

					if (File.separator.equals("/")) {

						destFileName = destDir
								+ compressFileName.replaceAll("\\\\", "/");

						destDirName = destFileName.substring(0, destFileName
								.lastIndexOf("/"));

						// windowsϵͳ

					} else {

						destFileName = destDir
								+ compressFileName.replaceAll("/", "\\\\");

						destDirName = destFileName.substring(0, destFileName
								.lastIndexOf("\\"));

					}

					// 2�����ļ���

					File dir = new File(destDirName);

					if (!dir.exists() || !dir.isDirectory()) {

						dir.mkdirs();

					}

					// 3��ѹ���ļ�

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
	 * ��ѹ��
	 * 
	 * @param sourceFile
	 *            Դ�ļ�
	 * @param destDir
	 *            Ŀ¼
	 * @throws Exception
	 *             �쳣
	 */
	/**
	public static void deCompress(String sourceFile, String destDir)
			throws Exception {
		// ��֤�ļ���·�������"/"����"\"
		char lastChar = destDir.charAt(destDir.length() - 1);
		if (lastChar != '/' && lastChar != '\\') {
			destDir += File.separator;
		}

		// �������ͣ�������Ӧ�Ľ�ѹ��
		String type = sourceFile.substring(sourceFile.lastIndexOf(".") + 1);
		if (type.equals("zip")) {
			DeCompressUtil.unzip(sourceFile, destDir);
		} else if (type.equals("rar")) {
			DeCompressUtil.unrar(sourceFile, destDir);
		} else
			throw new Exception("ֻ֧��zip��rar��ʽ��ѹ������");

	}
	
	*/

	/**
     * ѹ�������ļ����е������ļ�������ָ�����Ƶ�zipѹ����
     * @param filepath �ļ�����Ŀ¼
     * @param zippath ѹ����zip�ļ�����
     * @param dirFlag zip�ļ��е�һ���Ƿ����һ��Ŀ¼��true������falseû��
     */
    public static void zipMultiFile(String filepath ,String zippath, boolean dirFlag) {
        try {
            File file = new File(filepath);// Ҫ��ѹ�����ļ���
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
			DeCompressUtil.unzip(("D:/��������/SplitFile158.zip"),"D:/��������1/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		DeCompressUtil.zipMultiFile("C:/Users/yy/Desktop/�������հ�", "D://zz.zip", true);
	}


}

