package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlDateTypeHandler extends BaseTypeHandler {

  public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setDate(i, (java.sql.Date) parameter);
  }

  public Object getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    return rs.getDate(columnName);
  }

  public Object getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return cs.getDate(columnIndex);
  }

}
