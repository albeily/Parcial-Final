from zeep import Client
from flask import Flask, request, render_template, Response
import json

app = Flask(__name__, template_folder='resources', static_folder='resources', static_url_path='')
url = "https://abeily.me/api/soap/form?wsdl"
client = Client(url)

###############
#   Classes   #
###############
class XForm(object):
    def __init__(self, id, user, person, sector, education, location):
        self.id = id
        self.user_username = user.username
        self.person_name = person.name
        self.person_lastname = person.lastname
        self.person_photo_uri = person.photo.uri
        self.sector = sector
        self.education = education
        self.location_latitude = location.latitude
        self.location_longitude = location.longitude

class Form(object):
    def __init__(self, id, user, person, sector, education, location):
        self.id = id
        self.user = user
        self.person = person
        self.sector = sector
        self.education = education
        self.location = location

class User(object):
    def __init__(self, username):
        self.username = username

class Person(object):
    def __init__(self, name, lastname, photo):
        self.name = name
        self.lastname = lastname
        self.photo = photo

class Photo(object):
    def __init__(self, uri):
        self.uri = uri

class Location(object):
    def __init__(self, latitude, longitude):
        self.latitude = latitude
        self.longitude = longitude

##############
#   Server   #
##############

@app.route('/', methods=['GET'])
def page():    
    return render_template("html/client.html")

@app.route('/form', methods=['GET'])
def getAll():
    result = client.service.getAll()

    forms = []

    for form in result:
        photo = Photo(form.person.photo.uri)
        person = Person(form.person.name, form.person.lastname, photo)
        location = Location(form.location.latitude, form.location.longitude)
        user = User(form.user.username)
        data = XForm(form.id, user, person, form.sector, form.education, location)
        forms.append(data.__dict__)

    response = Response(response=json.dumps(forms), status=200, mimetype="application/json")
    
    return response

@app.route('/form', methods=['POST'])
def submit():    
    json_data = request.get_json(force = True)
    
    client.service.submit(json_data)
    
    return redirect("/");

if __name__ == '__main__':
    app.run(debug=False, host='localhost', port='5000')











