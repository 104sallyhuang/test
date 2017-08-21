package com.m104.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WriteService {

	public WriteService() {
		// TODO Auto-generated constructor stub
	}

	public void UpdateMdmKey(Connection conn, String autono, String mdmkey)
			throws SQLException, ClassNotFoundException {
		String selectSql = "UPDATE newcustomer SET newcust_crm_id=? WHERE autonumber=?";
		PreparedStatement pstmtUpdate = conn.prepareStatement(selectSql);
		pstmtUpdate.clearParameters();
		pstmtUpdate.setString(1, mdmkey);
		pstmtUpdate.setString(2, autono);
		pstmtUpdate.executeUpdate();
		pstmtUpdate.close();

	}

}
