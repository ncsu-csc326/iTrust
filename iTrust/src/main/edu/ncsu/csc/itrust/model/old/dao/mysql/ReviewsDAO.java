package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ReviewsBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ReviewsBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Provide a way to handle database queries for the reviews table, store HCP
 * review information.
 */
public class ReviewsDAO {
	private DAOFactory factory;
	private ReviewsBeanLoader loader;

	/**
	 * The basic constructor for the ReviewsDAO object.
	 * 
	 * @param factory
	 *            DAOFactory entry param
	 */
	public ReviewsDAO(DAOFactory factory) {
		this.factory = factory;
		loader = new ReviewsBeanLoader();
	}

	/**
	 * Based on the information from the ReviewsBean, add a review for an HCP
	 * (given by HCP in the bean) into the reviews table.
	 * 
	 * @param bean
	 *            containing the rating
	 * @return true if the review was added successfully and false otherwise
	 * @throws DBException
	 */
	public boolean addReview(ReviewsBean bean) throws DBException {
		if (bean == null) {
			return false;
		}
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = loader.loadParameters(
						conn.prepareStatement(
								"INSERT INTO reviews(mid, pid, reviewdate, descriptivereview, rating, title)  VALUES (?,?,(CURRENT_TIMESTAMP),?,?,?)"),
						bean)) {
			boolean added = ps.executeUpdate() > 0;
			return added;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Get a list of all reviews for a given HCP with id matching input param
	 * pid.
	 * 
	 * @param pid
	 *            ID of the HCP whose reviews to return
	 * @return list of all reviews, null if there aren't any
	 * @throws DBException
	 */
	public List<ReviewsBean> getAllReviews(long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM reviews WHERE pid=?")) {
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();

			return loader.loadList(rs);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Get a list of all reviews for a given HCP with id matching input param
	 * pid.
	 * 
	 * @param pid
	 *            ID of the HCP whose reviews to return
	 * @return list of all reviews, null if there aren't any
	 * @throws DBException
	 */
	public List<ReviewsBean> getAllReviews() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM reviews");
				ResultSet rs = ps.executeQuery()) {
			List<ReviewsBean> records = loader.loadList(rs);
			return records;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Get a list of all reviews for a given HCP with id matching input param
	 * pid.
	 * 
	 * @param pid
	 *            ID of the HCP whose reviews to return
	 * @return list of all reviews, null if there aren't any
	 * @throws DBException
	 */
	public List<ReviewsBean> getReviews(long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM reviews WHERE pid=?")) {

			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			List<ReviewsBean> records = loader.loadList(rs);
			return records;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Get average rating for the given HCP overall, all categories and all
	 * reviews.
	 * 
	 * @param pid
	 *            HCP with ratings wanted
	 * @return the average rating of all the overall ratings
	 * @throws DBException
	 */
	public double getTotalAverageRating(long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT AVG(rating) FROM reviews WHERE pid=?")) {
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();

			double averageRating = rs.next() ? rs.getDouble("AVG(rating)") : 0;
			rs.close();
			return averageRating;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * This checks the appointment table in the DB to see if the there is an
	 * appointment entry for the input mid(patient_id) and pid(doctor_id)
	 * params. It returns true if the patient has seen the given doctor, false
	 * otherwise.
	 * 
	 * @param mid
	 *            Patient ID
	 * @param pid
	 *            HCP ID
	 * @return true if the patient has had an appointment with the HCP, false
	 *         otherwise
	 * @throws DBException
	 */
	public boolean isRateable(long mid, long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM appointment WHERE patient_id =? AND doctor_id=?")) {
			ps.setLong(1, mid);
			ps.setLong(2, pid);
			ResultSet rs = ps.executeQuery();
			boolean isRatable = rs.next();
			return isRatable;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
