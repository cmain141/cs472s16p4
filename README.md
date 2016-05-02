Description

Your job is to write a console application for initiating and managing concurrent web downloads.
Functional Requirements (3 points)

In summary, this application allows the user to enter complete URLs of files to be downloaded. Multiple downloads can be active at the same time, and the user can control each one independently. Because GUI programming would exceed the scope of this course, we are limiting ourselves to a console-based application.

The application should prompt the user for input and supports the following input choices:

    URL of the form http://... starts downloading the resource at the given URL (in the background)
    l shows a numbered list of active and completed downloads with bytes read, total bytes, and percentage for each download
    c <number> cancels the download with the given number
    q quits the application

The application notifies the user when any of the following events occurs:

    download completed
    download failed

The application should handle errors gracefully by printing a concise error message and re-prompting the user. In particular, your application should handle the following possible errors:

    invalid input choice
    invalid URL
    resource not found at given URL

Implementation/Nonfunctional Requirements (1.5 points)

The nonfunctional requirements are as follows:

    Please follow this example.
    Use instances of AtomicInteger to keep track of the number of bytes read and the total number of bytes in a thread-safe way.
    Obtain the total number of bytes from the header.
    Include meaningful testing.

Be sure strictly to separate the application functionality from the console-based user interface in your application architecture. In particular, each of the two layers should be defined in a separate module with its own source file.
Documentation (0.5 point)

Provide concise Scaladoc (/** ... */) and inline (// or /*...*/) comments where appropriate.
Extra credit

    (0.5 point) Add the capability to suspend and resume ongoing downloads; provide these additional input choices to support this capability:
        s <number> suspends the desired download
        r <number> resumes the desired download
    (0.5 point) Add the capability to put one specific download at a time in the "foreground" (setting it as the current download) and get a text-based horizontal progress bar for it; provide these additional input choices to support this capability:
        f <number> puts the desired download in the foreground (designate as the current download)
        b puts the current download in the background
        x cancels the current download
    (0.5 point) Report any unexpected download status, such as 404 (not found) and 301/302 (redirect) as part of your list of downloads.
    (1 point) Use RxScala instead of futures. See also this documentation. Wrap a custom "cold" Observable around the download and produce a sequence of progress events. Talk to me if you want to pursue this route.
