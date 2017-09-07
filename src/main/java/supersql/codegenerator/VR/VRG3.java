/*
 * Created on 2004/07/25
 */
package supersql.codegenerator.VR;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.stringtemplate.v4.compiler.STParser.ifstat_return;

import supersql.codegenerator.Grouper;
import supersql.codegenerator.Jscss;
import supersql.codegenerator.Manager;
import supersql.codegenerator.Modifier;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.common.Utils;
import supersql.extendclass.ExtList;

public class VRG3 extends Grouper {

	private String backfile = new String();
	private int countinstance = 0;

	private VREnv vr_env;
	private VREnv vr_env2;
	boolean retFlag = false;	// 20140611_masato pagenationフラグ

	// ���󥹥ȥ饯��
	public VRG3(Manager manager, VREnv henv, VREnv henv2) {
		this.vr_env = henv;
		this.vr_env2 = henv2;
	}


	@Override
	public String getSymbol() {
		return "VRG3";
	}

	// G3��work�᥽�å�
	@Override
	public String work(ExtList data_info) {
		int count = 0;		// 20140526_masato
		int count2 = 0;		// 20140526_masato
		int i = 0;			// 20140526_masato
		int j = 0;			// 20140526_masato
		int k = 0;	
		if (decos.containsKey("vr_x")) {
			i = Integer.parseInt(decos.getStr("vr_x"));
			retFlag = true;
			if(!VRAttribute.componexflag){
				VRAttribute.compx[VRAttribute.cgcount] = i;
				VRAttribute.compflag[VRAttribute.cgcount] = 3;
			}
			VRAttribute.componexflag = true;
		}
		if (decos.containsKey("vr_y")) {///column->row_x, row->vr_y
			j = Integer.parseInt(decos.getStr("vr_y"));
			retFlag = true;
			if(!VRAttribute.componeyflag){
				VRAttribute.compy[VRAttribute.cgcount] = j;
				VRAttribute.compflag[VRAttribute.cgcount] = 3;
			}
			VRAttribute.componeyflag = true;
		}		
		if (decos.containsKey("vr_z")) {
			k = Integer.parseInt(decos.getStr("vr_z"));
			retFlag = true;
			if(!VRAttribute.componezflag){
				VRAttribute.compz[VRAttribute.cgcount] = k;
				VRAttribute.compflag[VRAttribute.cgcount] = 3;
			}
			VRAttribute.componezflag = true;
		}

		Log.out("------- G3 -------");

		this.setDataList(data_info);

		if(vr_env.gLevel == 0){
			VRAttribute.floorarray.add(3);
		}
		if(vr_env.gLevel == 1){
			VRAttribute.exharray.add(3);
		}
		
		//System.out.println("<G3front>");
		VRAttribute.gjudge++;
		
		while (this.hasMoreItems()==true) {
			//////////////////////////G22//////////////////////////
			VRAttribute.genre = "";

			count++;
			VRAttribute.seq = 0;//n2 kotani
			
			try {
				int l=VRManager.gindex.get(vr_env.gLevel);
				VRManager.gindex.set(vr_env.gLevel,l+1);//gindex[]++
			} catch (Exception e) {
				VRManager.gindex.add(1);	//gindex[]=1
			}
			
			vr_env.gLevel++;
			Log.out("selectFlg" + VREnv.getSelectFlg());
			Log.out("selectRepeatFlg" + VREnv.getSelectRepeat());
			Log.out("formItemFlg" + VREnv.getFormItemFlg());
			
			String classid = VREnv.getClassID(tfe);

			if (GlobalEnv.isOpt() && !VREnv.getSelectRepeat()) {
				vr_env2.code.append("<tfe type=\"repeat\" dimension=\"2\"");
				vr_env2.code.append(" border=\"" + vr_env.tableBorder
						+ "\"");

				if (decos.containsKey("tablealign"))
					vr_env2.code.append(" align=\""
							+ decos.getStr("tablealign") + "\"");
				if (decos.containsKey("tablevalign"))
					vr_env2.code.append(" valign=\""
							+ decos.getStr("tablevalign") + "\"");

				if (decos.containsKey("class")) {
					// class=menu�Ȃǂ̎w�肪��������t��
					vr_env2.code.append(" class=\"");
					vr_env2.code.append(decos.getStr("class") + " ");
				}
				if (vr_env.writtenClassId.contains(VREnv.getClassID(this))) {
					// TFE10000�Ȃǂ̎w�肪��������t��
					if (decos.containsKey("class")) {
						vr_env2.code.append(VREnv.getClassID(this) + "\"");
					} else {
						vr_env2.code.append(" class=\""
								+ VREnv.getClassID(this) + "\"");
					}
				} else if (decos.containsKey("class")) {
					vr_env2.code.append("\"");
				}

				if (decos.containsKey("tabletype")) {
					vr_env2.code.append(" tabletype=\""
							+ decos.getStr("tabletype") + "\"");
					if (decos.containsKey("cellspacing")) {
						vr_env2.code.append(" cellspacing=\""
								+ decos.getStr("cellspacing") + "\"");
					}
					if (decos.containsKey("cellpadding")) {
						vr_env2.code.append(" cellpadding=\""
								+ decos.getStr("cellpadding") + "\"");
					}
				}
				vr_env2.code.append(">");
			}

			this.worknextItem();

			if (vr_env.notWrittenClassId.contains(classid)
					&& vr_env.code.indexOf(classid) >= 0) {
				vr_env.code.delete(vr_env.code.indexOf(classid),
						vr_env.code.indexOf(classid) + classid.length() + 1);
			}

			if (VREnv.getSelectRepeat()) {

			} else {
				// chie
				vr_env2.code.append("</tfe>");
			}
			
			vr_env.gLevel--;	
		}
		VRManager.gindex.set(vr_env.gLevel, 0);
		if(vr_env.gLevel == 0){
			VRManager.nest1count++;
		}
		
		
//		int idcount = this.tfe.countconnectitem();//kotani ここからhalken装飾子処理　idcountの処理は一応残しとく		
//		if(Modifier.decoflag){
//			idcount -=  1;///idの回数 1は装飾子の分
//		}
//		for(int l=0; l<idcount;l++){//ここで何回もn2ごとにぐるぐるする
		if(Modifier.decoflag){
			for(int l=0; l<VRAttribute.elearray.size()-1;l++){//1は装飾子の分を無駄に数えてるから引く
				vr_env.code.append("<n2 seq=\""+l+"\">\n" );
				vr_env.code.append(VRAttribute.elearray.get(l));
				if(!VRAttribute.decovalue.isEmpty()){
					vr_env.code.append(" <name>"+VRAttribute.decovalue+"</name></element>\n");
				}
				vr_env.code.append("</n2>\n" );			
			}
		}else{
			for(int l=0; l<VRAttribute.elearray.size();l++){//ここで何回もn2ごとにぐるぐるする
				vr_env.code.append("<n2 seq=\""+l+"\">\n" );
				vr_env.code.append(VRAttribute.elearray.get(l));
				if(!VRAttribute.decovalue.isEmpty()){
					vr_env.code.append(" <name>"+VRAttribute.decovalue+"</name></element>\n");
				}
				vr_env.code.append("</n2>\n" );			
			}
		}
		
		VRAttribute.decovalue = "";
		VRDecoration.ends = new ArrayList<StringBuffer>();
		VRAttribute.elearray.clear();//初期化
		VRAttribute.seq = 0;//初期化
		Modifier.decoflag = false;//ここまでhalken装飾子処理

		
//		for(int l=0; l<VRAttribute.elearray.size();l++){///n2 kotani　怖いから一応残しとく
//			vr_env.code.append("<n2 seq=\""+l+"\">\n" );
//			vr_env.code.append(VRAttribute.elearray.get(l));
//			vr_env.code.append("</n2>\n" );			
//		}
//		VRAttribute.elearray.clear();//初期化
//		VRAttribute.seq = 0;//初期化
		
		if(VRAttribute.gjudge==1){
			VRAttribute.billnum++;
		}
		VRAttribute.gjudge--;
		
			/////////////////////////G22end//////////////////////

		if(vr_env.gLevel == 0){
			VRAttribute.componexflag = false;
			VRAttribute.componeyflag = false;
			VRAttribute.componezflag = false;
			VRAttribute.cgcount++;
			
			vr_env.code.append("</group>\n");
			VRAttribute.grouptag++;
				vr_env.code.append("<group>\n");
			VRAttribute.genrearray22.add(VRAttribute.genrecount);
		}
		
		return null;

	}
}
