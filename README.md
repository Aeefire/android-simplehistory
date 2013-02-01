android-simplehistory
=====================

SimpleHistoryLib provides a very easy way to get a download history going in your android application.

By adding this project as library to your android project, a database will be created automatically.

Afterwards use a  HistoryHelper(Context ctx) instance to do all operations. To insert / update a database-object,
an Entry object is needed.

The HistoryLoadedListener will only be called when using a method to retrieve multiple history entries.

Only use Entry.setId() when you want to update an object.

....more to come....

This project is licensed under the Apache License v2.0 <br>
<code>http://www.apache.org/licenses/LICENSE-2.0 </code>
