package com.farrow.knmiddleware.daos.jdbc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.farrow.knmiddleware.utils.ExceptionUtils;
import com.farrow.knmiddleware.daos.AbstractFssbfdDaoJdbc;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.SourceSystem;
import com.farrow.knmiddleware.exceptions.FileMissingException;
import com.farrow.knmiddleware.exceptions.QueueItemLockedException;

@Repository
public class QueueDaoJdbc extends AbstractFssbfdDaoJdbc{
	
	private static final Logger log = LogManager.getLogger(QueueDaoJdbc.class);
	
	
	public Integer saveNewQueueItem(QueueItem item) throws SQLException {
		Integer id = null;
		try (Connection con = dataSource.getConnection();) {
			
			String query = "insert into queue (sourceSystem ,dataType ,inputFileId ,objectFileId, outputXmlId) values (?,?,?,?,?)";
			try (PreparedStatement stmt = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);) {
				stmt.setString(1, item.getSourceSystem().toString());
				stmt.setString(2, item.getSourceSystem().toString());
				if(item.getInputFile()!=null) {
					item.getInputFile().setId(saveInputFile(con,item.getInputFile()));
					stmt.setInt(3, item.getInputFile().getId());
				}
				else {
					stmt.setNull(3, Types.INTEGER);
				}
				if(item.getObjectFile()!=null) {
					item.getObjectFile().setId(saveObjectFile(con,item.getObjectFile()));
					stmt.setInt(4, item.getObjectFile().getId());
				}
				else {
					stmt.setNull(4, Types.INTEGER);
				}
				if(item.getOutputXml()!=null) {
					item.getOutputXml().setId(saveOutputFile(con,item.getOutputXml()));
					stmt.setInt(5, item.getOutputXml().getId());
				}
				else {
					stmt.setNull(5, Types.INTEGER);
				}
				int affectedRows = stmt.executeUpdate();
				if (affectedRows == 0) {
		            throw new SQLException("Failed to create queueItem");
		        }

		        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		            	id = generatedKeys.getInt(1);
		            }
		            else {
		                throw new SQLException("Creating queue item failed, no ID obtained.");
		            }
		        }
			}
		}
		return id;
	}
	
	public Integer saveInputFile(Integer queueId, QueueFile file) throws SQLException {
		try (Connection con = dataSource.getConnection();) {
			Integer fileId = saveInputFile(con,file);
			String query = "UPDATE queue SET inputFileId=? WHERE id=?";
			try(PreparedStatement stmt = con.prepareStatement(query);){
				stmt.setInt(1, fileId);
				stmt.setInt(2, queueId);
				int affectedRows = stmt.executeUpdate();
				if (affectedRows == 0) {
		            throw new SQLException("Failed to save input file");
		        }
			}
			return fileId;
		}
	}
	
	private Integer saveInputFile(Connection con, QueueFile file) throws SQLException {
		try (PreparedStatement fileStmt = con.prepareStatement("INSERT INTO queueInputFile (textcontent) values(?) ",Statement.RETURN_GENERATED_KEYS);){
			fileStmt.setString(1, new String(file.getFile(), StandardCharsets.UTF_8));
			int affectedRows = fileStmt.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Failed to create input file");
	        }
			try (ResultSet generatedKeys = fileStmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					file.setId(generatedKeys.getInt(1));
					return file.getId();
				}
				else {
					throw new SQLException("Creating input file failed, no ID obtained.");
				}
		    }
		}
	}
	public Integer saveObjectFile(Integer queueId, QueueFile file) throws SQLException {
		try (Connection con = dataSource.getConnection();) {
			Integer fileId = saveObjectFile(con,file);
			String query = "UPDATE queue SET objectFileId=? WHERE id=?";
			try(PreparedStatement stmt = con.prepareStatement(query);){
				stmt.setInt(1, fileId);
				stmt.setInt(2, queueId);
				int affectedRows = stmt.executeUpdate();
				if (affectedRows == 0) {
		            throw new SQLException("Failed to save object file");
		        }
			}
			return fileId;
		}
	}
	
	private Integer saveObjectFile(Connection con, QueueFile file) throws SQLException {
		try (PreparedStatement fileStmt = con.prepareStatement("INSERT INTO queueObjectFile (textcontent) values(?) ",Statement.RETURN_GENERATED_KEYS);){
			fileStmt.setString(1, new String(file.getFile(), StandardCharsets.UTF_8));
			int affectedRows = fileStmt.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Failed to create input file");
	        }
			try (ResultSet generatedKeys = fileStmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					file.setId(generatedKeys.getInt(1));
					return file.getId();
				}
				else {
					throw new SQLException("Creating input file failed, no ID obtained.");
				}
		    }
		}
	}
	public Integer saveOutputFile(Integer queueId, QueueFile file) throws SQLException {
		try (Connection con = dataSource.getConnection();) {
			Integer fileId = saveOutputFile(con,file);
			String query = "UPDATE queue SET outputXmlId=? WHERE id=?";
			try(PreparedStatement stmt = con.prepareStatement(query);){
				stmt.setInt(1, fileId);
				stmt.setInt(2, queueId);
				int affectedRows = stmt.executeUpdate();
				if (affectedRows == 0) {
		            throw new SQLException("Failed to save output file");
		        }
			}
			return fileId;
		}
	}
	
	private Integer saveOutputFile(Connection con, QueueFile file) throws SQLException {
		try (PreparedStatement fileStmt = con.prepareStatement("INSERT INTO queueOutputFile (textcontent) values(?) ",Statement.RETURN_GENERATED_KEYS);){
			fileStmt.setString(1, new String(file.getFile(), StandardCharsets.UTF_8));
			int affectedRows = fileStmt.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Failed to create input file");
	        }
			try (ResultSet generatedKeys = fileStmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					file.setId(generatedKeys.getInt(1));
					return file.getId();
				}
				else {
					throw new SQLException("Creating input file failed, no ID obtained.");
				}
		    }
		}
	}
	
	
	private QueueItem getQueueItem(Integer id) throws SQLException, FileMissingException {
		try (Connection con = dataSource.getConnection();) {
			String queryStr = "SELECT * FROM queue WHERE id = ?";
			try (PreparedStatement stmt = con.prepareStatement(queryStr);) {
				stmt.setInt(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					if(rs.next()) {
						QueueItem queueItem = new QueueItem();
						queueItem.setId(rs.getInt("id"));
						queueItem.setSourceSystem(SourceSystem.valueOf(rs.getString("sourceSystem")));
						queueItem.setDataType(DataType.valueOf(rs.getString("dataType")));
						queueItem.setCreated(rs.getTimestamp("created").toLocalDateTime());
						queueItem.setConversionRunOn(rs.getString("conversionRunOn"));
						Timestamp conversionCompleted = rs.getTimestamp("conversionCompleted");
						if(conversionCompleted!=null) {
							queueItem.setConversionCompleted(conversionCompleted.toLocalDateTime());
						}
						Timestamp conversionStarted = rs.getTimestamp("conversionStarted");
						if(conversionStarted!=null) {
							queueItem.setConversionStarted(conversionStarted.toLocalDateTime());
						}
						queueItem.setTransmissionRunOn(rs.getString("transmissionRunOn"));
						Timestamp transmissionStarted = rs.getTimestamp("transmissionStarted");
						if(transmissionStarted!=null) {
							queueItem.setTransmissionStarted(transmissionStarted.toLocalDateTime());
						}
						Timestamp transmissionCompleted = rs.getTimestamp("transmissionCompleted");
						if(transmissionCompleted!=null) {
							queueItem.setTransmissionCompleted(transmissionCompleted.toLocalDateTime());
						}
						Integer inputFileId = rs.getInt("inputFileId");
						if(inputFileId != null) {
							try(PreparedStatement inputFileStmt = con.prepareStatement("SELECT * FROM queueInputFile WHERE id = ?")){
								inputFileStmt.setInt(1, inputFileId);
								try(ResultSet rsInputFile = inputFileStmt.executeQuery()){
									if(rs.next()) {
										QueueFile inputFile = new QueueFile();
										inputFile.setId(inputFileId);
										inputFile.setCreated(rs.getTimestamp("created").toLocalDateTime());
										inputFile.setFile(rs.getString("textcontent").getBytes(StandardCharsets.UTF_8));
										queueItem.setInputFile(inputFile);
									}
									else {
										throw new FileMissingException("Input file missing for "+queueItem.getId()+" input file id"+inputFileId);
									}
								}
								
							}
						}
						Integer objectFileId = rs.getInt("objectFileId");
						if(objectFileId != null) {
							try(PreparedStatement objectFileStmt = con.prepareStatement("SELECT * FROM queueObjectFile WHERE id = ?")){
								objectFileStmt.setInt(1, objectFileId);
								try(ResultSet rsInputFile = objectFileStmt.executeQuery()){
									if(rs.next()) {
										QueueFile objectFile = new QueueFile();
										objectFile.setId(objectFileId);
										objectFile.setCreated(rs.getTimestamp("created").toLocalDateTime());
										objectFile.setFile(rs.getString("textcontent").getBytes(StandardCharsets.UTF_8));
										queueItem.setObjectFile(objectFile);
									}
									else {
										throw new FileMissingException("Object file missing for "+queueItem.getId()+" object file id"+objectFileId);
									}
								}
								
							}
						}
						Integer outputXmlId = rs.getInt("outputXmlId");
						if(outputXmlId != null) {
							try(PreparedStatement outputFileStmt = con.prepareStatement("SELECT * FROM queueOutputFile WHERE id = ?")){
								outputFileStmt.setInt(1, outputXmlId);
								try(ResultSet rsInputFile = outputFileStmt.executeQuery()){
									if(rs.next()) {
										QueueFile outputFile = new QueueFile();
										outputFile.setId(outputXmlId);
										outputFile.setCreated(rs.getTimestamp("created").toLocalDateTime());
										outputFile.setFile(rs.getString("textcontent").getBytes(StandardCharsets.UTF_8));
										queueItem.setOutputXml(outputFile);
									}
									else {
										throw new FileMissingException("Output file missing for "+queueItem.getId()+" output file id"+outputXmlId);
									}
								}
								
							}
						}
						return queueItem;
 					}
					else {
						return null;
					}
				}
			}
		}
	}
	
	public QueueItem lockNextConversionJob() throws QueueItemLockedException, SQLException, FileMissingException {
		Integer id = null;
		try (Connection con = dataSource.getConnection();) {
			String queryStr = "SELECT id, conversionRunOn, conversionStarted FROM queue WHERE conversionStarted is null ORDER BY created ASC fetch first 1 row only";
			try (PreparedStatement stmt = con.prepareStatement(queryStr, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_UPDATABLE);) {
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						String conversionRunOn = rs.getString("conversionRunOn");
						Timestamp conversionStarted = rs.getTimestamp("conversionStarted");
						id = rs.getInt("id");
						if (conversionRunOn != null) {
							throw new QueueItemLockedException("Queue Item currently locked by " + conversionStarted + " for faxID: " + id);
						}
						// If row found update it.
						try {
							rs.updateString("conversionRunOn", InetAddress.getLocalHost().getHostName());
						} catch (UnknownHostException e) {
							rs.updateString("conversionRunOn", "UnknownHost");
						}
						rs.updateTimestamp("conversionStarted", new Timestamp(System.currentTimeMillis()));
						rs.updateRow();
						
					} 
				}
			}
		} catch (SQLException e) {
			log.error("Error while locking the next Incoming Doc", e);
			throw new QueueItemLockedException("Error while locking the next queue item");
		}
		if(id!=null) {
			return getQueueItem(id);
		}
		else {
			return null;
		}
	}

	public void completeConversion(Integer queueItemId) {
		String query = "UPDATE queue SET conversionCompleted=current_timestamp WHERE id = :queueId";
		MapSqlParameterSource data = new MapSqlParameterSource();
		data.addValue("queueId", queueItemId);
		jdbcTemplate.update(query, data);
	}

	public QueueItem lockNextDeliveryJob() throws QueueItemLockedException, SQLException, FileMissingException {
		Integer id = null;
		try (Connection con = dataSource.getConnection();) {
			String queryStr = "SELECT id, transmissionRunOn, transmissionStarted FROM queue WHERE conversionCompleted is not null and transmissionStarted IS NULL ORDER BY created ASC fetch first 1 row only";
			try (PreparedStatement stmt = con.prepareStatement(queryStr, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_UPDATABLE);) {
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						String transmissionRunOn = rs.getString("transmissionRunOn");
						Timestamp transmissionStarted = rs.getTimestamp("transmissionStarted");
						id = rs.getInt("id");
						if (transmissionRunOn != null) {
							throw new QueueItemLockedException("Queue Item currently locked by " + transmissionStarted + " for faxID: " + id);
						}
						// If row found update it.
						try {
							rs.updateString("transmissionRunOn", InetAddress.getLocalHost().getHostName());
						} catch (UnknownHostException e) {
							rs.updateString("transmissionRunOn", "UnknownHost");
						}
						rs.updateTimestamp("transmissionStarted", new Timestamp(System.currentTimeMillis()));
						rs.updateRow();
					} 
				}
			}
		} catch (SQLException e) {
			log.error("Error while locking the next Incoming Doc", e);
			throw new QueueItemLockedException("Error while locking the next queue item");
		}
		if(id!=null) {
			return getQueueItem(id);
		}
		else {
			return null;
		}
	}
	
	public void completeTransmission(Integer queueItemId) {
		String query = "UPDATE queue SET transmissionCompleted=current_timestamp WHERE id = :queueId";
		MapSqlParameterSource data = new MapSqlParameterSource();
		data.addValue("queueId", queueItemId);
		jdbcTemplate.update(query, data);
	}
	
	public void saveConversionException(Integer itemId, Exception e) {
		saveException (itemId,e,"CONVERSION");

	}
	
	public void saveTransmissionException(Integer itemId, Exception e) {
		saveException (itemId,e,"TRANSMISSION");
	}
	
	private void saveException(Integer itemId, Exception e, String type) {
		String query = "insert into queueExceptions (queueid ,exceptionRunType ,errorMessage ,stacktrace) values (:queueId,:type,:message,:stacktrace)";
		MapSqlParameterSource data = new MapSqlParameterSource();
		data.addValue("queueId", itemId);
		data.addValue("type", type);
		data.addValue("message", StringUtils.substring(e.getMessage(), 0, 1024));
		data.addValue("stacktrace", ExceptionUtils.getStackTrace(e));
		jdbcTemplate.update(query, data);
	}
	
	public void cleanupExceptions(LocalDateTime threshold) {
		String query = "delete from queueExceptions where created < :threshold";
		MapSqlParameterSource data = new MapSqlParameterSource();
		data.addValue("threshold", threshold);
		jdbcTemplate.update(query, data);
	}
	
	public void cleanupObjectFiles(LocalDateTime threshold) {
		String query = "delete from queueObjectFile where created < :threshold";
		MapSqlParameterSource data = new MapSqlParameterSource();
		data.addValue("threshold", threshold);
		jdbcTemplate.update(query, data);
	}
	
	public void cleanupInputFiles(LocalDateTime threshold) {
		String query = "delete from queueInputFile where created < :threshold";
		MapSqlParameterSource data = new MapSqlParameterSource();
		data.addValue("threshold", threshold);
		jdbcTemplate.update(query, data);
	}
	
	public void cleanupXmlFiles(LocalDateTime threshold) {
		String query = "delete from queueOutputFile where created < :threshold";
		MapSqlParameterSource data = new MapSqlParameterSource();
		data.addValue("threshold", threshold);
		jdbcTemplate.update(query, data);
	}
	
	public void cleanupQueue(LocalDateTime threshold) {
		String query = "delete from queue where created < :threshold";
		MapSqlParameterSource data = new MapSqlParameterSource();
		data.addValue("threshold", threshold);
		jdbcTemplate.update(query, data);
	}
}
