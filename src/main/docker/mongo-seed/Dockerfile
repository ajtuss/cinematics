FROM mongo

COPY movies.json /init.json
CMD mongoimport --host mongo --db cinematics --collection movie --type json --file /init.json --jsonArray
