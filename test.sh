echo  ""
echo "Opening hours:"
curl -X GET http://127.0.0.1:8080/zoo/openinghours

echo  ""
echo  ""
echo "Testing add animal"
curl -X PUT http://127.0.0.1:8080/zoo/animals/Foobar -d '{"species":"Chimpanzee","age":10,"favouriteFood":"Bananas"}' -H "Content-Type: application/json"
echo "OK"

echo  ""
echo "Testing get all animals"
curl -X GET http://127.0.0.1:8080/zoo/animals

echo  ""
echo  ""
echo "Testing get animal details"
curl -X GET http://127.0.0.1:8080/zoo/animals/Charly
echo  ""

