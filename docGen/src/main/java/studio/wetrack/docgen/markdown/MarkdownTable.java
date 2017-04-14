package studio.wetrack.docgen.markdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhanghong on 17/3/17.
 */
public class MarkdownTable {
    String[] columns;
    List<String[]> rows = new ArrayList<>();
    public MarkdownTable(String... columns){
        this.columns = columns;

    }

    public void addRow(String ... columns){
        rows.add(columns);
    }

    public String toString(){

        //header
        StringBuilder thStr = new StringBuilder();
        StringBuilder delStr = new StringBuilder();
        thStr.append('|');
        delStr.append('|');
        for (String col : columns) {
            thStr.append(col).append('|');
            char[] del = new char[col.length()];
            Arrays.fill(del, '-');
            delStr.append(del).append('|');
        }
        thStr.append('\n').append(delStr).append('\n');

        //rows
        for (String[] row : rows) {
            thStr.append('|');
            for(String column : row){
                thStr.append(column).append('|');
            }
            thStr.append('\n');
        }

        return thStr.toString();
    }
}
