import json
import os
import random
from datetime import timedelta, datetime

import requests

first_names = [
    "Alice", "Bob", "Charlie", "Diana", "Ethan", "Fiona", "George", "Hannah", "Ian", "Julia",
    "Kevin", "Laura", "Michael", "Nina", "Oliver", "Paula", "Quentin", "Rachel", "Samuel", "Tina",
    "Uma", "Victor", "Wendy", "Xavier", "Yasmine", "Zach", "Aaron", "Bianca", "Caleb", "Delia",
    "Eli", "Faith", "Gavin", "Hailey", "Isaac", "Jade", "Kai", "Lena", "Miles", "Nora",
    "Omar", "Penny", "Quinn", "Riley", "Sebastian", "Tara", "Ulysses", "Vera", "Wyatt", "Xena",
    "Yara", "Zane", "Adrian", "Bella", "Cameron", "Daisy", "Edward", "Farah", "Grayson", "Heidi",
    "Isla", "Jake", "Kira", "Leo", "Maya", "Noah", "Opal", "Peter", "Queenie", "Reid",
    "Sophie", "Tristan", "Ursula", "Valerie", "Wesley", "Ximena", "Yosef", "Zoe", "Amir", "Brielle",
    "Colin", "Daphne", "Emmett", "Gabriella", "Harvey", "Indira", "Jasper", "Kelsey", "Luca", "Melody",
    "Niko", "Olive", "Phoenix", "Rosa", "Silas", "Talia", "Vince", "Willa", "Yusuf", "Zelda"
]

last_names = [
    "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
    "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
    "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
    "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
    "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts",
    "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz", "Edwards", "Collins", "Reyes",
    "Stewart", "Morris", "Morales", "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper",
    "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson",
    "Watson", "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes",
    "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long", "Ross", "Foster", "Jimenez"
]

email_providers = [
    "gmail.com", "outlook.com", "yahoo.com", "icloud.com", "proton.me", "zoho.com", "aol.com", "gmx.com", "mail.com",
    "hotmail.com"
]

jobs = {
    "CEO": {
        "min": 1000000,
        "max": 1000000,
        "number_of_subordinates": 10,
        "subordinate_type": "DIR"
    },
    "DIR": {
        "min": 400000,
        "max": 600000,
        "number_of_subordinates": 10,
        "subordinate_type": "PM"
    },
    "PM": {
        "min": 200000,
        "max": 400000,
        "number_of_teams": 10
    },
    "TL": {
        "min": 150000,
        "max": 250000
    },
    "DEV": {
        "min": 100000,
        "max": 200000
    },
    "QA": {
        "min": 75000,
        "max": 125000
    },
    "BA": {
        "min": 75000,
        "max": 125000
    }
}

street_names = [
    "Maple Street", "Oak Avenue", "Pine Road", "Cedar Lane", "Elm Street", "Birch Drive", "Willow Way",
    "Cherry Street", "Ash Avenue", "Hickory Lane", "Magnolia Boulevard", "Sunset Drive", "Riverview Court",
    "Meadow Lane", "Hilltop Road", "Ocean View Drive", "Bayview Avenue", "Forest Path", "Valley Road", "Brookside Drive"
]

cities = [
    "Springfield", "Riverton", "Greenville", "Fairview", "Madison", "Clinton", "Georgetown", "Franklin",
    "Arlington", "Ashland", "Bristol", "Clayton", "Dayton", "Lexington", "Milton", "Norwood", "Oxford", "Salem",
    "Trenton", "Winchester"
]

states = [
    "NY", "CA", "TX", "FL", "IL", "PA", "OH", "GA", "NC", "MI",
    "WA", "CO", "AZ", "MA", "TN", "IN", "MO", "WI", "MN", "VA"
]

url = "http://localhost:8080/employees"

token = os.environ.get("TOKEN")

headers = {
    "Content-Type": "application/json",
    "Authorization": f"Bearer {token}"
}


class TokenExpiredException(Exception):
    pass


class UnknownException(Exception):
    pass


def random_us_phone():
    area_code = random.randint(200, 999)
    exchange = random.randint(200, 999)
    subscriber = random.randint(1000, 9999)

    return f"{area_code}-{exchange}-{subscriber}"


def random_date(start_date, end_date):
    delta = end_date - start_date
    random_days = random.randint(0, delta.days)
    rand_date = start_date + timedelta(days=random_days)

    return rand_date.strftime("%Y-%m-%d")


def create_employee(job_id: str, manager_id: str | None) -> str:
    global counter

    first_name = random.choice(first_names)
    last_name = random.choice(last_names)

    employee = {
        "first_name": first_name,
        "last_name": last_name,
        "email": first_name.lower() + "." + last_name.lower() + "@" + random.choice(email_providers),
        "phone_number": random_us_phone(),
        "hire_date": random_date(datetime(2000, 1, 1), datetime(2025, 5, 17)),
        "job_id": job_id,
        "salary": random.randint(jobs[job_id]["min"], jobs[job_id]["max"]),
        "manager_id": manager_id,
        "address": {
            "street": f"{random.randint(100, 9999)} {random.choice(street_names)}",
            "city": random.choice(cities),
            "state": random.choice(states),
            "zip_code": random.randint(10000, 99999)
        },
        "leave_balances": {
            "annual": random.randint(0, 30),
            "sick": random.randint(0, 30),
            "maternity": random.randint(0, 30),
            "paternity": random.randint(0, 30),
            "bereavement": random.randint(0, 30),
            "unpaid": random.randint(0, 30),
            "compensatory": random.randint(0, 30),
            "study": random.randint(0, 30),
            "casual": random.randint(0, 30),
            "sabbatical": random.randint(0, 30)
        }
    }

    payload = json.dumps(employee)
    response = requests.request("POST", url, headers=headers, data=payload)

    if response.status_code == 200:
        counter += 1
        employee = json.loads(response.text)
        employee_id = employee["id"]
        print(f"Employee number {counter} created with the following id: {employee_id}")
        return employee_id
    elif response.status_code == 403:
        print(f"Token expired while creating employee number {counter + 1}. Stopping execution...")
        raise TokenExpiredException()
    elif response.status_code == 500:
        print(f"Could not create employee number {counter + 1}. Retrying employee creation...")
        return create_employee(job_id, manager_id)
    else:
        print(f"Unknown error: [{response.status_code}][{response.text}]. Stopping execution...")
        raise UnknownException()


def create_employee_with_subordinates(job_id: str, manager_id: str | None):
    employee_id = create_employee(job_id, manager_id)

    if job_id == "PM":
        for _ in range(0, jobs["PM"]["number_of_teams"]):
            create_employee("TL", employee_id)
            create_employee("DEV", employee_id)
            create_employee("DEV", employee_id)
            create_employee("QA", employee_id)
            create_employee("BA", employee_id)
    else:
        for _ in range(0, jobs[job_id]["number_of_subordinates"]):
            create_employee_with_subordinates(jobs[job_id]["subordinate_type"], employee_id)


counter = 0
create_employee_with_subordinates("CEO", None)
