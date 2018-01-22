package models;

import java.util.Objects;

public class Review {
    private String writtenBy;
    private int rating;
    private int id;
    private int restaurantId; //i will be used to connect Restaurant to Review.
    private String content;

    public Review(String writtenBy, int rating, String content, int restaurantId) {
        this.writtenBy = writtenBy;
        this.rating = rating;
        this.restaurantId = restaurantId;
        this.content = content;
    }

    public String getWrittenBy() {
        return writtenBy;
    }

    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return rating == review.rating &&
                restaurantId == review.restaurantId &&
                Objects.equals(writtenBy, review.writtenBy) &&
                Objects.equals(content, review.content);
    }

    @Override
    public int hashCode() {

        return Objects.hash(writtenBy, rating, restaurantId, content);
    }
}
