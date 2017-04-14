package studio.wetrack.docgen.markdown;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.TypeVariable;

/**
 * Created by zhanghong on 17/3/9.
 */
public class MarkdownFile {

    static String H1 = "#%s\n";
    static String H2 = "##%s\n";
    static String H3 = "###%s\n";
    static String H4 = "####%s\n";
    static String CODE_SEG = "``\n%s\n``\n";

    String path;
    String fileName;
    File file;
    StringBuilder sb;

    public MarkdownFile(String path, String fileName){
        this.path = path;
        this.fileName = fileName;
        this.file = new File(path + fileName + ".md");
        this.sb = new StringBuilder();
    }

    public void writeH1(String content){
        sb.append("#").append(content).append('\n');
    }
    public void writeH2(String content){
        sb.append("##").append(content).append('\n');
    }
    public void writeH3(String content){
        sb.append("###").append(content).append('\n');
    }
    public void writeH4(String content){
        sb.append("####").append(content).append('\n');
    }

    public void writeCodeSeg(String content){
        sb.append(String.format(CODE_SEG, content));
    }

    public void write(String content){
        sb.append(content);
    }
    public void writeLine(String line){
        write(line + "\n");
    }

    public void generate() throws IOException {
        if(sb.length() == 0){
            return;
        }
        FileWriter writer = new FileWriter(path + "/" + fileName + ".md", false);
        writer.write(sb.toString());
        writer.flush();
    }

    public static void main(String[] args) throws IOException {
//        MarkdownTable mt = new MarkdownTable("col1", "col2", "col3");
//        mt.addRow("row1-col1", "row1-col2", "row1-col3");
//        mt.addRow("row2-col1", "row2-col2");
//        mt.addRow("row3-col1", "row3-col2", "row3-col3", "row3-col4");
//        System.out.println(mt.toString());
//
//
//        MarkdownFile file = new MarkdownFile("", "test");
//        file.writeH1("test h1");
//        file.writeH2("test h2");
//        file.writeH3("test h3");
//        file.writeH4("test h4");
////        file.write(mt.toString());
//
//        ClassWriter cw = new ClassWriter(ClassWriter.class);
//        file.writeCodeSeg(cw.toString());
//
//        file.generate();

        String strings =  "11";
        System.out.println("List<String> is Synthetic " + strings.getClass().isSynthetic());
        for (TypeVariable<? extends Class<? extends String>> typeVariable : strings.getClass().getTypeParameters()) {
            System.out.println("List<String> type variable type " + typeVariable.getGenericDeclaration().getName());

        }

    }
}
