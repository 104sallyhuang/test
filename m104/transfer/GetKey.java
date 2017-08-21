package com.m104.transfer;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;




 

public class GetKey {
	
	//新架構取號
		public String getMdmKey() throws ClassNotFoundException, SQLException {
			long keyVal;
			Date now = new Date();
			String keyName="mdmkey";
			Connection conn = DoConnection.getConnection("m064001_b0051_ai");
			String sSql = "Select get_no_date From sequence Where name = ? " ;
			PreparedStatement pstmt = conn.prepareStatement(sSql);
			pstmt.setString(1, keyName);
			ResultSet rs=pstmt.executeQuery();
			Date lastDate=null;
			if (rs.next()){
				lastDate = rs.getDate("get_no_date");
			}
			rs.close();
			pstmt.close();

			SimpleDateFormat FORM_NO_SDF = new SimpleDateFormat("yyMM");
			SimpleDateFormat LAST_ACCESS_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			if (lastDate == null || FORM_NO_SDF.format(lastDate).equalsIgnoreCase(FORM_NO_SDF.format(now))) {
				sSql="UPDATE sequence SET current_value = LAST_INSERT_ID(current_value + 1), get_no_date = '" + LAST_ACCESS_SDF.format(now) + "' WHERE  name = '"+keyName+"' ";
				pstmt = conn.prepareStatement(sSql);
				pstmt.clearParameters();
				pstmt.executeUpdate();
				pstmt.close();

			} else {
				// reset
				sSql="UPDATE sequence SET current_value = LAST_INSERT_ID(1), get_no_date = '" + LAST_ACCESS_SDF.format(now) + "' WHERE  name = '"+keyName+"' ";
				pstmt = conn.prepareStatement(sSql);
				pstmt.clearParameters();
				pstmt.executeUpdate();
				pstmt.close();

			}		
			sSql="SELECT LAST_INSERT_ID() no";
			pstmt = conn.prepareStatement(sSql);
			rs=pstmt.executeQuery();
			keyVal=0;
			if (rs.next()){
				keyVal = rs.getLong(1);
			}
			rs.close();
			pstmt.close();
			DecimalFormat FORM_NO_DF = new DecimalFormat("000000000000");
			String  mdmKey = FORM_NO_SDF.format(new Date()) + FORM_NO_DF.format(keyVal);
			conn.close();
			return mdmKey;
		}
}
