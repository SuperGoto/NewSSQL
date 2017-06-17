package supersql.ctab;

import java.util.FormatFlagsConversionMismatchException;
import java.util.Hashtable;

import supersql.common.Log;
import supersql.extendclass.ExtList;

public class Ctab {
	public ExtList makeCtab(ExtList tfe){
		Log.info("start_tfe:"+tfe);
		//tfe's number is lt 3
		if(tfe.size() < 7){
			System.err.println("cross_tab function argument is insufficient. Three arguments are required.");
			return tfe;
		}
		
		//separate tfe arg
		ExtList tfe1 = (ExtList)tfe.get(2);
		ExtList tfe2 = (ExtList)tfe.get(4);
		ExtList tfe3 = (ExtList)tfe.get(6);
		Log.info("tfe1:"+tfe1);
		Log.info("tfe2:"+tfe2);
		Log.info("tfe3:"+tfe3);
		
		//add sorting information. add ascend sort.
		//if there exists, than we do nothing.
//		Hashtable<String, String> sort_info = new Hashtable<String, String>(); //the pair of attribute name and sort info
		ExtList tfe1_sorted = addSort(tfe1, 0);
		ExtList tfe2_sorted = addSort(tfe2, 0);
		Log.info("tfe1_sorted:"+tfe1_sorted);
		Log.info("tfe2_sorted:"+tfe2_sorted);
		
		//add null to tfe2
		ExtList tfe2_addnull = addNull(tfe2, 0);
		Log.info("tfe2_null:"+tfe2_addnull);
		
		//merge tfe2 and tfe3
		//if tfe2 is tree -> [tfe2, [tfe3],],
		//if tfe2 is forest -> {tfe2}, tfe3
		
		return tfe;
	}

	private ExtList addNull(ExtList tfe, int flag) {
		// TODO 自動生成されたメソッド・スタブ
		if(tfe.get(0).equals("operand")){
			if(((ExtList)((ExtList)tfe.get(1)).get(0)).get(0).equals("sorting")){
				ExtList tmp = new ExtList();				
				ExtList tmp2 = new ExtList();
				ExtList tmp3 = new ExtList();
				ExtList tmp4 = new ExtList();
				ExtList tmp5 = new ExtList();
				ExtList tmp6 = new ExtList();
				ExtList tmp7 = new ExtList();
				ExtList tmp8 = new ExtList();
				tmp.add("null");
				tmp2.add("keyword");
				tmp2.add(tmp);
				tmp3.add(tmp2);
				tmp4.add("function_name");
				tmp4.add(tmp3);
				tmp5.add(tmp4);
				tmp5.add("(");
				tmp5.add(tfe);
				tmp5.add(")");
				tmp6.add("function");
				tmp6.add(tmp5);
				tmp7.add("operand");
				tmp8.add(tmp6);
				tmp7.add(tmp8);
				return tmp7;
			}else if(((ExtList)((ExtList)tfe.get(1)).get(0)).get(0).equals("function")){
				ExtList tmp = new ExtList();
				tmp = addNull(((ExtList)((ExtList)((ExtList)((ExtList)tfe.get(1)).get(0)).get(1)).get(2)), 0);
				ExtList tmp2 = new ExtList();
				ExtList tmp3 = new ExtList();
				ExtList tmp4 = new ExtList();
				ExtList tmp5 = new ExtList();
				tmp2.add("operand");
				tmp3.add(((ExtList)((ExtList)((ExtList)((ExtList)tfe.get(1)).get(0)).get(1)).get(0)));
				tmp3.add("(");
				tmp3.add(tmp);
				tmp3.add(")");
				tmp4.add("function");
				tmp4.add(tmp3);
				tmp5.add(tmp4);
				tmp2.add(tmp5);
				return tmp2;
			}else{
				ExtList tmp = new ExtList();
				tmp.add("operand");
				tmp.add(addNull((ExtList)tfe.get(1), 1));
				return tmp;
			}
		}else if(tfe.get(0).equals("{")){
			ExtList tmp = new ExtList();
			ExtList tmp2 = new ExtList();
			tmp.add(tfe.get(0));
			tmp.add(addNull((ExtList)tfe.get(1), 1));
			tmp.add("}");
			return tmp;
		}else if(tfe.get(0).equals("grouper")){
			ExtList tmp = new ExtList();
			ExtList tmp2 = new ExtList();
			tmp.add("[");
			tmp.add(addNull((ExtList)((ExtList)tfe.get(1)).get(1), 0));
			tmp.add("]");
			tmp.add(",");
			tmp2.add("grouper");
			tmp2.add(tmp);
			return tmp2;
		}else if(tfe.get(0).equals("exp")){
			ExtList tmp = new ExtList();
			tmp.add("exp");
			tmp.add(addNull((ExtList)tfe.get(1), 1));
			return tmp;
		}else if(tfe.get(0).equals("d_exp") || tfe.get(0).equals("v_exp") || tfe.get(0).equals("h_exp")){
			ExtList tmp = new ExtList();
			ExtList tmp2 = new ExtList();
			int child_num = 0;
			child_num = ((ExtList)tfe.get(1)).size() / 2 + 1;
			for(int i = 0; i < child_num; i++){
				if(i != 0){
					tmp.add(((ExtList)tfe.get(1)).get(2 * i - 1));
				}
				tmp.add(addNull((ExtList)((ExtList)tfe.get(1)).get(2 * i), 0));
			}
			tmp2.add(tfe.get(0));
			tmp2.add(tmp);
			return tmp2;
		}else{
			ExtList tmp = new ExtList();
			if(flag == 0){
				tmp.add(tfe.get(0));
				tmp.add(addNull((ExtList)tfe.get(1), 1));
			}else if(flag == 1){
				tmp.add(addNull((ExtList)tfe.get(0), 0));
			}
			return tmp;
		}
	}

	private ExtList addSort(ExtList tfe, int flag) {
		// TODO 自動生成されたメソッド・スタブ
		if(tfe.get(0).equals("operand")){
			if(((ExtList)((ExtList)tfe.get(1)).get(0)).get(0).equals("sorting")){
				return tfe;
			}else if(((ExtList)((ExtList)tfe.get(1)).get(0)).get(0).equals("function")){
				ExtList tmp = new ExtList();
				tmp = addSort(((ExtList)((ExtList)((ExtList)((ExtList)tfe.get(1)).get(0)).get(1)).get(2)), 0);
				ExtList tmp2 = new ExtList();
				ExtList tmp3 = new ExtList();
				ExtList tmp4 = new ExtList();
				ExtList tmp5 = new ExtList();
				tmp2.add("operand");
				tmp3.add(((ExtList)((ExtList)((ExtList)((ExtList)tfe.get(1)).get(0)).get(1)).get(0)));
				tmp3.add("(");
				tmp3.add(tmp);
				tmp3.add(")");
				tmp4.add("function");
				tmp4.add(tmp3);
				tmp5.add(tmp4);
				tmp2.add(tmp5);
				return tmp2;
			}else if(((ExtList)((ExtList)tfe.get(1)).get(0)).get(0).equals("table_alias") || ((ExtList)((ExtList)tfe.get(1)).get(0)).get(0).equals("column_name")){
				ExtList tmp = new ExtList();
				ExtList tmp2 = new ExtList();
				tmp.add("(");
				tmp.add("asc");
				tmp.add(")");
				tmp2.add("sorting");
				tmp2.add(tmp);
				((ExtList)tfe.get(1)).add(0, tmp2);
				return tfe;
			}else{
				ExtList tmp = new ExtList();
				tmp.add("operand");
				tmp.add(addSort((ExtList)tfe.get(1), 1));
				return tmp;
			}
		}else if(tfe.get(0).equals("{")){
			ExtList tmp = new ExtList();
			ExtList tmp2 = new ExtList();
			tmp.add(tfe.get(0));
			tmp.add(addSort((ExtList)tfe.get(1), 1));
			tmp.add("}");
			return tmp;
		}else if(tfe.get(0).equals("grouper")){
			ExtList tmp = new ExtList();
			ExtList tmp2 = new ExtList();
			tmp.add("[");
			tmp.add(addSort((ExtList)((ExtList)tfe.get(1)).get(1), 0));
			tmp.add("]");
			tmp.add(((ExtList)tfe.get(1)).get(3));
			tmp2.add("grouper");
			tmp2.add(tmp);
			return tmp2;
		}else if(tfe.get(0).equals("exp")){
			ExtList tmp = new ExtList();
			tmp.add("exp");
			tmp.add(addSort((ExtList)tfe.get(1), 1));
			return tmp;
		}else if(tfe.get(0).equals("d_exp") || tfe.get(0).equals("v_exp") || tfe.get(0).equals("h_exp")){
			ExtList tmp = new ExtList();
			ExtList tmp2 = new ExtList();
			int child_num = 0;
			child_num = ((ExtList)tfe.get(1)).size() / 2 + 1;
			for(int i = 0; i < child_num; i++){
				if(i != 0){
					tmp.add(((ExtList)tfe.get(1)).get(2 * i - 1));
				}
				tmp.add(addSort((ExtList)((ExtList)tfe.get(1)).get(2 * i), 0));
			}
			tmp2.add(tfe.get(0));
			tmp2.add(tmp);
			return tmp2;
		}else{
			ExtList tmp = new ExtList();
			if(flag == 0){
				tmp.add(tfe.get(0));
				tmp.add(addSort((ExtList)tfe.get(1), 1));
			}else if(flag == 1){
				tmp.add(addSort((ExtList)tfe.get(0), 0));
			}
			return tmp;
		}
	}
}
