package com.swaruph.RookTownBot.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.swaruph.RookTownBot.config.DatabaseConfig;
import com.swaruph.RookTownBot.model.LeaderboardPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RookDB {

    private static final Logger logger = LoggerFactory.getLogger(RookDB.class);
    
    DatabaseConfig db = new DatabaseConfig();

    private final String BOT_DISCORD_ID = "1310486108483878983";

    public RookDB() {
        createRookTable();
    }

    public void createRookTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS rook (
                    puuid TEXT PRIMARY KEY,
                    discord_id TEXT,
                    name TEXT,
                    agents TEXT,
                    totalRounds INTEGER,
                    totalMatches INTEGER,
                    rating REAL,
                    ACS REAL,
                    KDA REAL,
                    KAST INTEGER,
                    ADR REAL,
                    KPR REAL,
                    APR REAL,
                    FKPR REAL,
                    FDPR REAL,
                    HS INTEGER,
                    CL INTEGER,
                    CLWP REAL,
                    KMAX INTEGER,
                    kills INTEGER,
                    deaths INTEGER,
                    assists INTEGER,
                    FK INTEGER,
                    FD INTEGER,
                    wins INTEGER,
                    loses INTEGER
                );
        """;

        try(
                Connection con = db.connect();
                PreparedStatement pstmt = con.prepareStatement(query)
        ){
            pstmt.executeUpdate();
        }catch (SQLException e){
            logger.error("Failed to create rook table", e);
        }

        db.execute(query);
    }

    public void insertIntoRook(String puuid, String discordId, String name) {
        String query = "INSERT INTO rook (puuid, discord_id, name) VALUES (?, ?, ?)";

        try (
                Connection con = db.connect();
                PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            pstmt.setString(1, puuid);
            pstmt.setString(2, discordId);
            pstmt.setString(3, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to insert into rook", e);
        }
    }

    public String getDiscordIdByPuuid(String puuid) {
        String discordId = null;
        String query = "SELECT discord_id FROM rook WHERE puuid = ?";

        try(
                Connection con = db.connect();
                PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            pstmt.setString(1, puuid);
            ResultSet resultSet = pstmt.executeQuery();
            discordId = resultSet.getString("discord_id");
        } catch (SQLException e) {
            logger.error("Failed to get discord id by puuid", e);
        }

        // if the discordId is null, return a bot's discord id
        if(discordId==null){
            discordId = BOT_DISCORD_ID;
            return discordId;
        }
        return discordId;
    }

    public String getNameByDiscordId(String discordId) {
        String name;
        String query = "SELECT name FROM rook WHERE discord_id = ?";

        try(
                Connection con = db.connect();
                PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            pstmt.setString(1, discordId);
            ResultSet resultSet = pstmt.executeQuery();
            name = resultSet.getString("name");
            return name;
        } catch (SQLException e) {
            logger.error("Failed to get name by discord id", e);
            throw new RuntimeException("Failed to get name by discord id", e);
        }
    }

    public void insertIntoLeaderboard(String puuid) {
        String query = "INSERT INTO rook (puuid) VALUES (?)";

        try(
                Connection con = db.connect();
                PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            pstmt.setString(1, puuid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to insert into leaderboard", e);
        }

    }

    public void updateLeaderboardStats(LeaderboardPlayer leaderboard) {
        String sql =  "INSERT OR REPLACE INTO rook (puuid, discord_id, name, agents, totalRounds, totalMatches, rating, ACS, KDA, KAST, ADR, KPR, APR, FKPR, FDPR, HS, CL, CLWP, KMAX, kills, deaths, assists,FK, FD, wins, loses) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(
                Connection con = db.connect();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pstmt.setString(1, leaderboard.getPuuid());
            pstmt.setString(2, leaderboard.getDiscordId());
            pstmt.setString(3, leaderboard.getLeaderboardPlayerName());
            pstmt.setString(4, leaderboard.getAgents());
            pstmt.setInt(5, leaderboard.getTotalRounds());
            pstmt.setInt(6, leaderboard.getTotalMatches());
            pstmt.setDouble(7, leaderboard.getRating());
            pstmt.setDouble(8, leaderboard.getACS());
            pstmt.setDouble(9, leaderboard.getKDA());
            pstmt.setInt(10, leaderboard.getKAST());
            pstmt.setDouble(11, leaderboard.getADR());
            pstmt.setDouble(12, leaderboard.getKPR());
            pstmt.setDouble(13, leaderboard.getAPR());
            pstmt.setDouble(14, leaderboard.getFKPR());
            pstmt.setDouble(15, leaderboard.getFDPR());
            pstmt.setInt(16, leaderboard.getHS());
            pstmt.setInt(17, leaderboard.getCL());
            pstmt.setDouble(18, leaderboard.getCLWP());
            pstmt.setInt(19, leaderboard.getKMAX());
            pstmt.setInt(20, leaderboard.getKills());
            pstmt.setInt(21, leaderboard.getDeaths());
            pstmt.setInt(22, leaderboard.getAssists());
            pstmt.setInt(23, leaderboard.getFK());
            pstmt.setInt(24, leaderboard.getFD());
            pstmt.setInt(25, leaderboard.getWins());
            pstmt.setInt(26, leaderboard.getLoses());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update leaderboard stats", e);
        }

    }

    public LeaderboardPlayer getLeaderboardPlayer(String puuid) {
        String sql = "SELECT * FROM rook WHERE puuid = ?";
        LeaderboardPlayer leaderboardPlayer = new LeaderboardPlayer(puuid);

        try(
                Connection con = db.connect();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ){
            pstmt.setString(1, puuid);
            ResultSet resultSet = pstmt.executeQuery();
            leaderboardPlayer.setLeaderboardPlayerName(resultSet.getString("name"));
            leaderboardPlayer.setDiscordId(resultSet.getString("discord_id"));
            leaderboardPlayer.setAgents(resultSet.getString("agents"));
            leaderboardPlayer.setTotalRounds(resultSet.getInt("totalRounds"));
            leaderboardPlayer.setTotalMatches(resultSet.getInt("totalMatches"));
            leaderboardPlayer.setRating(resultSet.getDouble("rating"));
            leaderboardPlayer.setACS(resultSet.getDouble("ACS"));
            leaderboardPlayer.setKDA(resultSet.getDouble("KDA"));
            leaderboardPlayer.setKAST(resultSet.getInt("KAST"));
            leaderboardPlayer.setADR(resultSet.getDouble("ADR"));
            leaderboardPlayer.setKPR(resultSet.getDouble("KPR"));
            leaderboardPlayer.setAPR(resultSet.getDouble("APR"));
            leaderboardPlayer.setFKPR(resultSet.getDouble("FKPR"));
            leaderboardPlayer.setFDPR(resultSet.getDouble("FDPR"));
            leaderboardPlayer.setHS(resultSet.getInt("HS"));
            leaderboardPlayer.setCL(resultSet.getInt("CL"));
            leaderboardPlayer.setCLWP(resultSet.getDouble("CLWP"));
            leaderboardPlayer.setKMAX(resultSet.getInt("KMAX"));
            leaderboardPlayer.setKills(resultSet.getInt("kills"));
            leaderboardPlayer.setDeaths(resultSet.getInt("deaths"));
            leaderboardPlayer.setAssists(resultSet.getInt("assists"));
            leaderboardPlayer.setFK(resultSet.getInt("FK"));
            leaderboardPlayer.setFD(resultSet.getInt("FD"));
            leaderboardPlayer.setWins(resultSet.getInt("wins"));
            leaderboardPlayer.setLoses(resultSet.getInt("loses"));
            resultSet.close();
        } catch (SQLException e) {
            logger.error("Failed to get leaderboard player", e);
        }

        return leaderboardPlayer;
    }

    public List<LeaderboardPlayer> getAllLeaderboardStats() {
        String query = "SELECT * FROM rook";
        List<LeaderboardPlayer> leaderboardPlayers = new ArrayList<>();

        try(
                Connection con = db.connect();
                PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                LeaderboardPlayer leaderboardPlayer = new LeaderboardPlayer(resultSet.getString("puuid"));
                leaderboardPlayer.setLeaderboardPlayerName(resultSet.getString("name"));
                leaderboardPlayer.setDiscordId(resultSet.getString("discord_id"));
                leaderboardPlayer.setAgents(resultSet.getString("agents"));
                leaderboardPlayer.setTotalRounds(resultSet.getInt("totalRounds"));
                leaderboardPlayer.setTotalMatches(resultSet.getInt("totalMatches"));
                leaderboardPlayer.setRating(resultSet.getDouble("rating"));
                leaderboardPlayer.setACS(resultSet.getDouble("ACS"));
                leaderboardPlayer.setKDA(resultSet.getDouble("KDA"));
                leaderboardPlayer.setKAST(resultSet.getInt("KAST"));
                leaderboardPlayer.setADR(resultSet.getDouble("ADR"));
                leaderboardPlayer.setKPR(resultSet.getDouble("KPR"));
                leaderboardPlayer.setAPR(resultSet.getDouble("APR"));
                leaderboardPlayer.setFKPR(resultSet.getDouble("FKPR"));
                leaderboardPlayer.setFDPR(resultSet.getDouble("FDPR"));
                leaderboardPlayer.setHS(resultSet.getInt("HS"));
                leaderboardPlayer.setCL(resultSet.getInt("CL"));
                leaderboardPlayer.setCLWP(resultSet.getDouble("CLWP"));
                leaderboardPlayer.setKMAX(resultSet.getInt("KMAX"));
                leaderboardPlayer.setKills(resultSet.getInt("kills"));
                leaderboardPlayer.setDeaths(resultSet.getInt("deaths"));
                leaderboardPlayer.setAssists(resultSet.getInt("assists"));
                leaderboardPlayer.setFK(resultSet.getInt("FK"));
                leaderboardPlayer.setFD(resultSet.getInt("FD"));
                leaderboardPlayer.setWins(resultSet.getInt("wins"));
                leaderboardPlayer.setLoses(resultSet.getInt("loses"));
                leaderboardPlayers.add(leaderboardPlayer);
            }
            resultSet.close();
        } catch (SQLException e) {
            logger.error("Failed to get all leaderboard stats", e);
        }

        return leaderboardPlayers;
    }

    public String getPlayerNameByPuuid(String puuid) {
        String name = null;
        String query = "SELECT name FROM rook WHERE puuid = ?";

        try(
                Connection con = db.connect();
                PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            pstmt.setString(1, puuid);
            ResultSet resultSet = pstmt.executeQuery();
            name = resultSet.getString("name");
            resultSet.close();
        } catch (SQLException e) {
            logger.error("Failed to get player name by puuid", e);
        }

        return name;
    }
}
