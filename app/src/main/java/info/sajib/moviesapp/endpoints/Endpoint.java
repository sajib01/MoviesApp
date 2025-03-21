package info.sajib.moviesapp.endpoints;

/**
 * Created by sajib on 06-03-2016.
 */
public interface Endpoint {
    String baseURL = "https://api.themoviedb.org/3";
    String DISCOVER = baseURL + "/discover/movie";
    String SEARCH = baseURL + "/search";
    String MOVIE = baseURL + "/movie/";
    String IMAGE = "https://image.tmdb.org/t/p";
    String GENRE= baseURL + "/genre/movie/list";
    String PERSON=baseURL + "/person/";
    String YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/";
    String TV=baseURL + "/tv/";
}
