package supersql.extendclass;


import supersql.common.GlobalEnv;
import supersql.parser.FromInfo;
import supersql.parser.WhereInfo;
import supersql.parser.WhereParse;

import java.util.*;

public class QueryBuffer {
    private ExtList schf;
    public ExtList sep_sch;
    private HashSet tg;
    private FromInfo fi;
    private Hashtable atts;
    private ExtList aggregate_list;
    private ExtList aggregate_attnum_list;
    private ExtList result;
    private String query;
    public ExtList constructedResult;
//    private ExtList orderTable = Preprocessor.getOrderByTable();
    public int forestNum = 0; //Which tree belongs to in forest
    public int treeNum = 0; //集約によって分割される前はどの木にいたか


    public QueryBuffer(ExtList schf){
//        ArrayList<Integer> tmp = new ArrayList<>();
//        for (int i = 0; i < schf.unnest().size(); i++) {
//            tmp.add((int)schf.unnest().get(i));
//        }
//        tmp.sort(Comparator.naturalOrder());
//        this.schf = new ExtList();
//        for (int i: tmp) {
//            this.schf.add(i);
//        }
        schf = schf.unnest();
        schf.sort(Comparator.naturalOrder());
        this.schf = new ExtList(schf);
    }

    public void setFromInfo(String line){
        fi = new FromInfo(line);
    }

    public void setTg(HashSet tg){
        this.tg = tg;
    }

    public ExtList getSchf(){
        return schf;
    }

    public HashSet getTg(){
        return tg;
    }

    public FromInfo getFi() {
        return fi;
    }

    public void setAtts(Hashtable atts) {
        this.atts = atts;
    }

    public Hashtable getAtts() {
        return atts;
    }

    public void setAggregate_list(ExtList aggregate_list) {
        this.aggregate_list = aggregate_list;
    }

    public ExtList getAggregate_list() {
        return aggregate_list;
    }

    public void setAggregate_attnum_list(ExtList aggregate_attnum_list) {
        this.aggregate_attnum_list = aggregate_attnum_list;
    }

    public void setResult(ExtList result) {
        this.result = new ExtList(result);
    }

    public ExtList getResult() {
        return result;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public ExtList getAggregate_attnum_list() {
        return aggregate_attnum_list;
    }

    public void makeQuery(WhereInfo where){
        Boolean flag = false;
        StringBuffer buf = new StringBuffer();
        //SELECT句作成
        //make SELECT clause
        buf.append("SELECT ");
        boolean isAgg = false;
//        boolean isOrder = false;
        boolean containAgg = false;
//        String orderStr = new String();
        schf.sort(Comparator.naturalOrder());
        ArrayList<String> usedTables = new ArrayList<>();
        for(int index = 0; index < this.schf.size(); index++){
            int attnum = (Integer)this.schf.get(index);
//            isOrder = false;
//            for (int i = 0; i < orderTable.size(); i++) {
//                String ordert = orderTable.get(i).toString();
//                if(ordert.substring(ordert.indexOf("["), ordert.indexOf("]") + 1).equals("["+attnum+"]")){
//                    orderStr = ordert.substring(0, ordert.indexOf("]") + 1);
//                    isOrder = true;
//                }
//            }
            String att = atts.get(attnum).toString();
            if (!usedTables.contains(att.split("\\.")[0])){
                usedTables.add(att.split("\\.")[0]);
            }
            isAgg = false;
            String func_att = new String();
            if(this.aggregate_attnum_list.contains(attnum)){
                //集約だったらfunction_name(att)の形にして追加
                //if the attribute is to be aggregated, add by form of aggregation.
                String function_name = new String();
                String agg_str = new String();
                for(Object o: aggregate_list) {
                    if (Integer.parseInt(o.toString().split(" ")[0]) == attnum) {
                        agg_str = o.toString();
                    }
                }
                function_name = agg_str.split(" ")[1];
                func_att = function_name + "(" + att + ")";
                isAgg = true;
                containAgg = true;
            }
            if(!isAgg){
                //集約じゃなかったらそのまま追加
                //if Not aggregation, add as it is.
                if(index == 0){
                    buf.append(att);
                }else{
                    buf.append(", "+att);
                }
            }else{
                if(index == 0){
                    buf.append(func_att);
                }else{
                    buf.append(", "+func_att);
                }
            }
        }


//        System.out.println("usedTables:::"+usedTables);
        ArrayList<String> relatedTables = new ArrayList<>();
        for (int i = 0; i < usedTables.size(); i++) {
            ArrayList<String> relatedtables = GlobalEnv.relatedTableSet.get(usedTables.get(i));
            for (int j = 0; j < relatedtables.size(); j++) {
                findUsedTables(usedTables.get(i), relatedtables.get(j), GlobalEnv.relatedTableSet.get(relatedtables.get(j)), usedTables);
            }
        }
//        System.out.println("relatedTables:::"+relatedTables);
//        System.out.println("relateSet:::"+GlobalEnv.relatedTableSet);
//        System.out.println("usedTables_after:::"+usedTables);

        //FROM句作成
        //make From clause
        buf.append(" FROM ");
        String fClauseBefore =this.fi.getLine();
        String fClauseAfter = new String();
        for (String tb: fClauseBefore.split(",")) {
            String tAlias = tb.split(" ")[1];
            if(usedTables.contains(tAlias)){
                fClauseAfter += tb;
                fClauseAfter += ",";
            }
        }
        if(fClauseAfter.charAt(fClauseAfter.length() - 1) == ','){
            fClauseAfter = fClauseAfter.substring(0, fClauseAfter.length() - 1);
        }
        buf.append(fClauseAfter);


        //WHERE句作成
        //make Where clause
        Iterator e2 = where.getWhereClause().iterator();
        boolean first = true;
        while (e2.hasNext()) {
            WhereParse whe = (WhereParse) e2.next();
//            System.out.println("where clause:::"+whe.getUseTables());
            boolean addFlag = true;
            Iterator ut = whe.getUseTables().iterator();
            while(ut.hasNext()){
                String tname = ut.next().toString();
                if(!usedTables.contains(tname)){
                    addFlag = false;
                    break;
                }
            }
            if(addFlag){
                if(first){
                    buf.append(" WHERE ");
                    buf.append(whe.getLine());
                    first = false;
                }else{
                    buf.append(" AND ");
                    buf.append(whe.getLine());
                }
            }
        }

        //Group By句作成
        //make Group By clause
        if(containAgg) {
            buf.append(" GROUP BY ");
            int j = 0;
            for (Object attnum : this.schf) {
                if (!this.aggregate_attnum_list.contains(attnum)) {
                    if (j == 0) {
                        buf.append(atts.get((int) attnum).toString());
                        j++;
                    } else {
                        buf.append(", " + atts.get((int) attnum).toString());
                    }
                }
            }
        }

        buf.append(";");

        this.query = buf.toString();
    }

    private Boolean findUsedTables(String parent, String now, ArrayList<String> child, ArrayList<String> usedTables) {
        if(usedTables.contains(now)){
            return true;
        }
        for (int i = 0; i < child.size(); i++) {
            if(!child.get(i).equals(parent)){
                boolean result = findUsedTables(now, child.get(i), GlobalEnv.relatedTableSet.get(child.get(i)), usedTables);
                if(result){
                    if(!usedTables.contains(now)){
                        usedTables.add(now);
                    }
                    return result;
                }
            }
        }
        return false;
    }

    public void showDebug(){
        System.out.println("Forest Num is "+this.forestNum);
        System.out.println("Tree Num is "+this.treeNum);
        System.out.println("sep_sch is "+this.sep_sch);
        System.out.println("SQL Query is "+this.getQuery());
        System.out.println("Result is "+this.getResult());
        System.out.println("Constructed Result is "+this.constructedResult);

    }
}
