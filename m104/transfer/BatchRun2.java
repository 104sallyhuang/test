package com.m104.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.skjegstad.utils.BloomFilter;

public class BatchRun2 {
	private static final Logger log = Logger.getLogger(BatchRun2.class);

	public BatchRun2() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// 將AC table 先寫到 MDM2 db 中
		Connection fromConn = DoConnection.getConnection("m005009");
		Connection toConn = DoConnection.getConnection("m064001_b0051_ai");
		NewCustomer newcust = new NewCustomer();
		WriteMdm wm = new WriteMdm();
		WriteService ws = new WriteService();
		GetKey newKey = new GetKey();
		// 表示有身份証字號的，要和 MDM 舊核對
		int icount = 0;
// 下有 email 為無內容所以，所以要加密email
		String sql = "SELECT * FROM newcustomer  c where (c.email is null OR c.email='')  and  c.`status` <> 'success' and c.`status`<> 'del' limit 100";

		// String sql = "SELECT b.pid, familyName, firstName, sex, birthday,
		// cellphone, tel, telArea, postNum, address, identity ,e.email FROM
		// sns_basicinfo b left join sns_email e on b.pid=e.pid "
		// +" where e.isMain=1 and b.identity is not null and b.identity <> ''
		// and e.isVerified=1 order by e.updateDate desc limit 100";
		Statement stmt = fromConn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		log.info("New Customer Transfer Begin....");
		ResultSetMetaData rsmd=rs.getMetaData();
		Row2Dao<NewCustomer> rd=new Row2Dao<NewCustomer>(rsmd,NewCustomer.class);
		while (rs.next()) {
			newcust=rd.fetch(rs);
			wm.insertBNewCustomer(toConn, newcust);
			// 更新 Service 的 mdmkey
			ws.UpdateMdmKey(fromConn, newcust.getAutonumber(), newcust.getNewcust_crm_id());
			icount++;
		}
		rs.close();
		stmt.close();
		fromConn.close();
		toConn.close();
		log.info("New Customer Transfer End....");
		log.info("UPDATE:"+ icount + "筆。");

	}

}
