package com.m104.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.skjegstad.utils.BloomFilter;

public class GenIDBloom {

	public BloomFilter genBloomFilter(Connection con) {
		int allCount = 10000;
		BloomFilter filter = null;
		String strSql = "SELECT COUNT(*) as num FROM cust_master";
		PreparedStatement stmt;
		ResultSet rs;
		try {
			strSql = "SELECT COUNT(*) as num FROM cust_master  WHERE id_num is not null";
			stmt = con.prepareStatement(strSql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				allCount = rs.getInt(1);
			}
			rs.close();
			stmt.close();
			filter = new BloomFilter(0.0001, allCount);
			strSql = "SELECT id_num as num FROM cust_master WHERE id_num is not null";
			String idnum="";
			stmt = con.prepareStatement(strSql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				idnum = rs.getString(1);
				filter.add(idnum);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return filter;

	}

}
