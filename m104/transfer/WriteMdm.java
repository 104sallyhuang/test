package com.m104.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class WriteMdm {

	private HashMap<String, String> mapBStatusId;

	private GetKey gkey = new GetKey();

	public WriteMdm() {
		this.mapBStatusId = new HashMap<String, String>();
		this.mapBStatusId.put("1", "籌備處");
		this.mapBStatusId.put("2", "正常(核准設立)");
		this.mapBStatusId.put("3", "停業");
		this.mapBStatusId.put("4", "歇業/解散/廢止/撤銷");
		this.mapBStatusId.put("5", "非會員(刪除會員資料)");
		this.mapBStatusId.put("6", "失效");

	}

	public String toStr(Object sour, String init) {
		String dest;
		try {
			dest = sour.toString();
			if (dest == null)
				dest = init;
		} catch (Exception e) {
			dest = init;
		}
		return dest;
	}

	// 更新 keymap 的值
	public void writeAcKeyMapping(Connection conn, String mdmkey, String bu_pk) throws SQLException {

		String updateSql = "UPDATE key_mapping SET pid=? WHERE mdm_key=?";
		PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql);
		pstmtUpdate.clearParameters();
		pstmtUpdate.setString(1, bu_pk);
		pstmtUpdate.setString(2, mdmkey);
		int updateCount = pstmtUpdate.executeUpdate();
		pstmtUpdate.close();
		if (updateCount == 0) {
			// 如果沒更新的話，就要Insert
			updateSql = "INSERT INTO key_mapping (mdm_key,pid) VALUES (?,?)";
			pstmtUpdate = conn.prepareStatement(updateSql);
			pstmtUpdate.clearParameters();
			pstmtUpdate.setString(1, mdmkey);
			pstmtUpdate.setString(2, bu_pk);
			pstmtUpdate.executeUpdate();
			pstmtUpdate.close();
		}

	}

	/*
	 * 更新 cust_master 資料
	 */
	public void UpdateCCustomer(Connection conn, AcInfo customer) throws SQLException, ClassNotFoundException {
		PreparedStatement pstmtUpdate = null;
		String selectSql = "";
		if (customer.getIdentity() == null || customer.getIdentity().equals("")) {
			selectSql = "SELECT  mdm_key as cust_no FROM key_mapping m WHERE m.pid=?";
			pstmtUpdate = conn.prepareStatement(selectSql);
			pstmtUpdate.clearParameters();
			pstmtUpdate.setString(1, customer.getPid());
		} else {
			selectSql = "SELECT cust_no FROM cust_master m WHERE m.id_num=?";
			pstmtUpdate = conn.prepareStatement(selectSql);
			pstmtUpdate.clearParameters();
			pstmtUpdate.setString(1, customer.getIdentity());
		}

		ResultSet rs = pstmtUpdate.executeQuery();
		if (rs.next()) {
			String cust_no = rs.getString(1);
			String updateSql = "UPDATE CUST_MASTER SET " + "family_name=?,first_name=?,fun_no=?,addr=?,email=?,"
					+ "tel=?,sex=?,birth_day=?,country_code_1=?,mobile_1=?,"
					+ "status_id='2',status_name='正常(核准設立)',category='0', user_name='Init',update_date=NOW()"
					+ " WHERE cust_no=?";
			pstmtUpdate = conn.prepareStatement(updateSql);
			pstmtUpdate.clearParameters();
			pstmtUpdate.setString(1, customer.getFamilyName());
			pstmtUpdate.setString(2, customer.getFirstName());
			pstmtUpdate.setString(3, customer.getPostNum());
			pstmtUpdate.setString(4, customer.getAddress());
			pstmtUpdate.setString(5, customer.getEmail());
			// 因為只給一個電話
			// 因為只給一個電話
			String tel = "";
			if (this.toStr(customer.getTelArea(), "").equals("")) {
				tel = customer.getTel();
			} else {
				tel = customer.getTelArea() + "-" + customer.getTel();
			}
			pstmtUpdate.setString(6, tel);
			pstmtUpdate.setString(7, customer.getSex());
			pstmtUpdate.setString(8, customer.getBirthday());
			pstmtUpdate.setString(9, null);
			pstmtUpdate.setString(10, customer.getCellphone());
			pstmtUpdate.setString(11, cust_no);
			pstmtUpdate.executeUpdate();
			// 新增至key_mapping
			this.writeAcKeyMapping(conn, cust_no, customer.getPid());
		} else {
			InsertCCustomer(conn, customer);
		}
		rs.close();
		pstmtUpdate.close();

	}

	/*
	 * For ac 更新 cust_master 資料
	 */
	public void UpdateACCustomer(Connection conn, AcInfo customer) throws SQLException, ClassNotFoundException {
		PreparedStatement pstmtUpdate = null;
		String selectSql = "";

		selectSql = "SELECT  mdm_key as cust_no FROM key_mapping m WHERE m.pid=?";
		pstmtUpdate = conn.prepareStatement(selectSql);
		pstmtUpdate.clearParameters();
		pstmtUpdate.setString(1, customer.getPid());

		ResultSet rs = pstmtUpdate.executeQuery();
		if (rs.next()) {
			String cust_no = rs.getString(1);
			String updateSql = "UPDATE CUST_MASTER SET " + "family_name=?,first_name=?,fun_no=?,addr=?,email=?,"
					+ "tel=?,sex=?,birth_day=?,country_code_1=?,mobile_1=?,"
					+ "status_id='2',status_name='正常(核准設立)',category='0', user_name='Init',update_date=NOW()"
					+ " WHERE cust_no=?";
			pstmtUpdate = conn.prepareStatement(updateSql);
			pstmtUpdate.clearParameters();
			pstmtUpdate.setString(1, customer.getFamilyName());
			pstmtUpdate.setString(2, customer.getFirstName());
			pstmtUpdate.setString(3, customer.getPostNum());
			pstmtUpdate.setString(4, customer.getAddress());
			pstmtUpdate.setString(5, customer.getEmail());
			// 因為只給一個電話
			String tel = "";
			if (this.toStr(customer.getTelArea(), "").equals("")) {
				tel = customer.getTel();
			} else {
				tel = customer.getTelArea() + "-" + customer.getTel();
			}
			pstmtUpdate.setString(6, tel);
			pstmtUpdate.setString(7, customer.getSex());
			if (this.toStr(customer.getBirthday(), "").equals("")) {
				pstmtUpdate.setString(8, null);
			} else {
				pstmtUpdate.setString(8, customer.getBirthday());
			}
			pstmtUpdate.setString(9, null);
			pstmtUpdate.setString(10, customer.getCellphone());
			pstmtUpdate.setString(11, cust_no);
			pstmtUpdate.executeUpdate();
			// 新增至key_mapping
			this.writeAcKeyMapping(conn, cust_no, customer.getPid());
		} else {
			InsertCCustomer(conn, customer);
		}
		rs.close();
		pstmtUpdate.close();

	}

	/*
	 * 新增 cust_master 資料
	 */
	public void InsertCCustomer(Connection conn, AcInfo customer) throws SQLException, ClassNotFoundException {
		String updateSql = "INSERT INTO CUST_MASTER (" + "cust_no,id_num,cust_type_code,family_name,first_name,"
				+ "fun_no,addr, bu_id,bu_tr_pk,email," + "tel,sex,birth_day,country_code_1,mobile_1,"
				+ "status_id,status_name,category, user_name,is_approved, " + "progress,update_date ) VALUES("
				+ "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "'2','正常(核准設立)','0','Init','Y'," + "'F',NOW()  )";
		customer.setMdmkey(gkey.getMdmKey());
		PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql);
		pstmtUpdate.clearParameters();
		pstmtUpdate.setString(1, customer.getMdmkey());
		pstmtUpdate.setString(2, customer.getIdentity());
		pstmtUpdate.setString(3, "5");
		pstmtUpdate.setString(4, customer.getFamilyName());
		pstmtUpdate.setString(5, customer.getFirstName());

		pstmtUpdate.setString(6, customer.getPostNum());
		pstmtUpdate.setString(7, customer.getAddress());
		pstmtUpdate.setString(8, "ac");
		pstmtUpdate.setString(9, "ac_" + customer.getPid());
		pstmtUpdate.setString(10, customer.getEmail());
		// 因為只給一個電話
		String tel = "";
		if (this.toStr(customer.getTelArea(), "").equals("")) {
			tel = customer.getTel();
		} else {
			tel = customer.getTelArea() + "-" + customer.getTel();
		}
		pstmtUpdate.setString(11, tel);
		pstmtUpdate.setString(12, customer.getSex());
		if (this.toStr(customer.getBirthday(), "").equals("")) {
			pstmtUpdate.setString(13, null);
		} else {
			pstmtUpdate.setString(13, customer.getBirthday());
		}

		pstmtUpdate.setString(14, null);
		pstmtUpdate.setString(15, customer.getCellphone());

		pstmtUpdate.executeUpdate();
		pstmtUpdate.close();

		// 新增至key_mapping
		this.writeAcKeyMapping(conn, customer.getMdmkey(), customer.getPid());
		this.InsertACProfile(conn, customer.getMdmkey());

	}

	public void InsertACProfile(Connection conn, String cust_no) throws SQLException {
		PreparedStatement pstmtUpd = null;
		String selectSql = "Insert INTO c_profile (cust_no,product_id) VALUES (?,'ac')";
		pstmtUpd = conn.prepareStatement(selectSql);
		pstmtUpd.clearParameters();
		pstmtUpd.setString(1, cust_no);
		pstmtUpd.executeUpdate();
		pstmtUpd.close();
	}

	/*
	 * 更新 cust_master 資料
	 */
	public void DeleteCCustomer(Connection conn, AcInfo customer) throws SQLException, ClassNotFoundException {
		String selectSql = "SELECT mdm_key as cust_no FROM key_mapping m WHERE m.pid=?";
		PreparedStatement pstmtUpdate = conn.prepareStatement(selectSql);
		pstmtUpdate.clearParameters();
		pstmtUpdate.setString(1, customer.getPid());
		ResultSet rs = pstmtUpdate.executeQuery();
		if (rs.next()) {
			String cust_no = rs.getString(1);
			String updateSql = "UPDATE CUST_MASTER SET " + "family_name='',first_name='',fun_no='',addr='',email='',"
					+ "tel='',sex='',birth_day=null,country_code_1='',mobile_1='',"
					+ "status_id='5',status_name='非會員(刪除會員資料)',user_name='',update_date=NOW()" + " WHERE cust_no=?";
			pstmtUpdate = conn.prepareStatement(updateSql);
			pstmtUpdate.clearParameters();
			pstmtUpdate.setString(1, cust_no);
			pstmtUpdate.executeUpdate();
			// 新增至key_mapping
			this.writeAcKeyMapping(conn, cust_no, customer.getPid());
		} else {
			InsertCCustomer(conn, customer);
		}
		rs.close();
		pstmtUpdate.close();

	}

	public void insertBNewCustomer(Connection conn, NewCustomer customer) throws SQLException, ClassNotFoundException {
		
		// Service 統編使用整數，要注意前面的 0 會不見
		boolean haveVat=false;
		String vatNo="";
		String custCname="";
		String custTypeCode="";
		String custSubName="";
		String custSubType="1";

		if (!this.toStr(customer.getInvoice(),"").equals("")) {
			vatNo = "00000000" + customer.getInvoice();
			vatNo = vatNo.substring(vatNo.length() - 8, vatNo.length());
			haveVat=true;
		}
		if(!this.toStr(customer.getReal_coname(),"").equals("")){
			custCname=customer.getReal_coname().split("_")[0];
			if (customer.getReal_coname().split("_").length > 1) {
				custSubName=customer.getReal_coname().split("_")[1];
			} else {
				custSubType="0";
			}
		}else{
			if(!this.toStr(customer.getName(),"").equals("")){
				custCname=customer.getName().split("_")[0];
				if (customer.getName().split("_").length > 1) {
					custSubName=custCname=customer.getName().split("_")[1];
				} else {
					custSubType="0";
				}
			}
		}
		if(haveVat){
			custTypeCode= "1";
		}else{
			custTypeCode= "2";
		}

		String updateSql = "INSERT INTO CUST_VAT_MASTER ( "
				+ "cust_no,vat_no,cust_type_code,cust_cname,cust_ename,"
				+ "group_code,cust_sub,cust_sub_name,bu_id,user_name,"
				+ "is_approved,bu_tr_pk,progress,mapping_id,category,"
				+ "preparatory_name,status_id,status_name,cust_sub_type,fun_no,"
				+ "industry_id,industry,owner,update_date" + ") VALUES("
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?," 
				+ "?,?,?,?,?," 
				+ "?,?,?,?,?,"
				+ "?,?,?,NOW())";

		
		PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql);
		pstmtUpdate.clearParameters();
		//  取得新的MDM key
		String mdmkey = gkey.getMdmKey();
		customer.setNewcust_crm_id(mdmkey);
		pstmtUpdate.setString(1, customer.getNewcust_crm_id());

		pstmtUpdate.setString(2,vatNo);
		
		
		// 預設新客為無統編
		pstmtUpdate.setString(3, custTypeCode);
		pstmtUpdate.setString(4, custCname);
		// Service 無英文名
		pstmtUpdate.setString(5, null);
		
		pstmtUpdate.setString(6, null);
		// 預設為主公司
		pstmtUpdate.setString(7, "00000");
		pstmtUpdate.setString(8, custSubName);
		pstmtUpdate.setString(9, "104");
		pstmtUpdate.setString(10, "import");
		pstmtUpdate.setString(11, "Y");
		pstmtUpdate.setString(12, "104_" + customer.getAutonumber());
		// progress 應為 'F'
		pstmtUpdate.setString(13, "F");
		// mapping id 先存 null
		pstmtUpdate.setString(14, null);
		// 預設為 0.商機/Leads
		pstmtUpdate.setString(15, "0");
		
		
		pstmtUpdate.setString(16, customer.getName());
		pstmtUpdate.setString(17, "1");
		pstmtUpdate.setString(18, "籌備處");
		// 預設為0:總公司
		pstmtUpdate.setString(19, custSubType);
		pstmtUpdate.setString(20, customer.getCity_no());
		
		
		pstmtUpdate.setString(21, customer.getIndustry_no() );
		pstmtUpdate.setString(22, customer.getIndustry());
		pstmtUpdate.setString(23, customer.getBoss());

		pstmtUpdate.executeUpdate();
		pstmtUpdate.close();
		// 新增至key_mapping
		this.writeNewKeyMapping(conn, customer.getNewcust_crm_id(), customer.getAutonumber());
		this.insertBNewContact(conn, customer);

	}

	public void insertBNewContact(Connection conn, NewCustomer customer) throws SQLException {
		String updateSql = "INSERT INTO product_contact ( " 
	+ "cust_no,product_id,contact_id,first_name,email_1,"
	+ "fax,tel_1,ext_1,country_code,mobile," 
	+ "fun_no,addr,contact_type,disabled,create_time,create_user) "
	+ "VALUES(" + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,'0',NOW(),'init')";

		PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql);
		pstmtUpdate.clearParameters();

		pstmtUpdate.setString(1, customer.getNewcust_crm_id());
		pstmtUpdate.setString(2,"vip");
		pstmtUpdate.setString(3, customer.getNewcust_content_id());
		pstmtUpdate.setString(4, customer.getContact_person());
		pstmtUpdate.setString(5, customer.getEmail());
		
		pstmtUpdate.setString(6, customer.getFax());
		pstmtUpdate.setString(7,customer.getTelephone());
		pstmtUpdate.setString(8,customer.getExt());
		pstmtUpdate.setString(9,"");
		pstmtUpdate.setString(10, customer.getMobile());

		pstmtUpdate.setString(11, customer.getCity_no());
		pstmtUpdate.setString(12, customer.getAddress());
		//  預設為主要連絡人
		pstmtUpdate.setString(13, "1");
		pstmtUpdate.executeUpdate();
		pstmtUpdate.close();
	}

	// 更新 keymap 的值
	public void writeNewKeyMapping(Connection conn, String mdmkey, String autono) throws SQLException {

		String updateSql = "UPDATE key_mapping SET auto_no=?  WHERE mdm_key=?";
		PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql);
		pstmtUpdate.clearParameters();
		pstmtUpdate.setString(1, autono);

		pstmtUpdate.setString(2, mdmkey);
		int updateCount = pstmtUpdate.executeUpdate();
		pstmtUpdate.close();
		if (updateCount == 0) {
			// 如果沒更新的話，就要Insert
			updateSql = "INSERT INTO key_mapping (mdm_key,auto_no) VALUES (?,?)";
			pstmtUpdate = conn.prepareStatement(updateSql);
			pstmtUpdate.clearParameters();
			pstmtUpdate.setString(1, mdmkey);
			pstmtUpdate.setString(2, autono);
			pstmtUpdate.executeUpdate();
			pstmtUpdate.close();
		}

	}

}
