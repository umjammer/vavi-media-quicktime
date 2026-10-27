package quicktime.std.movies;

/**
 * The MovieProgress interface can be implemented to allow you to attach a progress handler to each movie.
 * The handler will be called whenever a long operation is underway.
 */
public interface MovieProgress {

    /**
     * The Movie Toolbox indicates the progress of the operation to your progress function.
     * @param theMovie Specifies the movie for this operation.
     * @param message Indicates why the Movie Toolbox called your function. The following values are valid:
     *
     *  movieProgressOpen Indicates the start of a long operation. This is always the first message sent to your function. Your function can use this message to trigger the display of your progress window.
     *  movieProgressUpdatePercent Passes completion information to your function. The Movie Toolbox repeatedly sends this message to your function. The percentDone parameter indicates the relative completion of the operation. You can use this value to update your progress window.
     *  movieProgressClose Indicates the end of a long operation. This is always the last message sent to your function. Your function can use this message as an indication to remove its progress window.
     * @param whatOperation Indicates the long operation that is currently underway. The following values are valid:
     *
     *   progressOpFlatten Your application has called the FlattenMovie or FlattenMovieData function (described on page 2-105 of Inside Macintosh: QuickTime  and page 2-107 of Inside Macintosh: QuickTime, respectively).
     *   progressOpInsertTrackSegment Your application has called the InsertTrackSegment function (described on page 2-262 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is assigned to the movie that contains the destination track.
     *   progressOpInsertMovieSegment Your application has called the InsertMovieSegment function (described on page 2-257 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is assigned to the destination movie.
     *   progressOpPaste Your application has called the PasteMovieSelection function (described on page 2-249 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is assigned to the destination movie.
     *   progressOpAddMovieSelection Your application has called the AddMovieSelection function (described on page 2-250 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is assigned to the destination movie. The Movie Toolbox calls the progress function that is assigned to the destination movie.
     *   progressOpCopy Your application has called the CopyMovieSelection function (described on page 2-248 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is assigned to the destination movie.
     *   progressOpCut Your application has called the CutMovieSelection function (described on page 2-247 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is assigned to the destination movie.
     *   progressOpLoadMovieIntoRam Your application has called the LoadMovieIntoRam function (described on page 2-140 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is assigned to the destination movie.
     *   progressOpLoadTrackIntoRam Your application has called the LoadTrackIntoRam function (described on page 2-142 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is assigned to the destination track.
     *   progressOpLoadMediaIntoRam Your application has called the LoadMediaIntoRam function (described on page 2-143 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is assigned to the destination media.
     *   progressOpImportMovie Your application has called the ConvertFileToMovieFile function (described on page 2-93 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is associated with the destination movie file. This flag is also used, as appropriate, for the PasteHandleIntoMovie functions (described on page 2-252 of Inside Macintosh: QuickTime).
     *   progressOpExportMovie Your application has called the ConvertMovieToFile function (described on page 2-95 of Inside Macintosh: QuickTime). The Movie Toolbox calls the progress function that is associated with the destination movie. This flag is also used, as appropriate, for the PutMovieIntoTypedHandle function (described on page 2-253 of Inside Macintosh: QuickTime).
     * @param percentDone Contains a floating pointe value indicating how far the operation has progressed. Its value is always between 0.0 and 1.0. This parameter is valid only when the message field is set to movieProgressUpdatePercent.
     * @return an error code - noErr (zero) if no error has occured, otherwise a valid error code
     */
    int execute(Movie paramMovie, int paramInt1, int paramInt2, float paramFloat);
}

