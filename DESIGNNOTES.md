# Persistence

We don't want to do massive serialisation to and from an SQL database. That's just wasteful and makes no real sense as
we are going to be joining entities via the database or searching the database for entities (we'd use a search engine
for that).

Serialisation and deserialisation is expensive, so let's try to avoid it as much as possible.

Does it make sense to use HDFS directly? Keep what we can in memory and let HDFS serialise to disk when needed. Is that possible?

What about Chronicle Map?

Or, can we be abstract and support multiple persistence mechanisms that don't require JPA? 