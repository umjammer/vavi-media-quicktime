package quicktime.std.movies.media;

import quicktime.std.comp.Component;

/**
 * The MediaHandler class implements the corresponding data structure of the Movie Toolbox.
 * Media handler components allow the Movie Toolbox to play a movie's data.
 * The Movie Toolbox, by itself, cannot read or write movie data.
 * Rather, media handlers perform input and output services on behalf of the Movie Toolbox.
 */
public class MediaHandler extends Component {

    MediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }
}

