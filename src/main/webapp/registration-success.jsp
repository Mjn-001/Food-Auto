<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Welcome to Food Auto</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap');
        
        body {
            font-family: 'Outfit', sans-serif;
            background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)), 
                        url('https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=2070&auto=format&fit=crop');
            background-size: cover;
            background-position: center;
            height: 100vh;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .card {
            background: white;
            padding: 50px;
            border-radius: 12px;
            text-align: center;
            max-width: 400px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.3);
        }

        .success-icon { font-size: 60px; color: #2ecc71; margin-bottom: 20px; }
        h1 { color: #1c1c1c; margin: 0 0 10px 0; }
        p { color: #666; margin-bottom: 30px; }

        .btn-login {
            display: inline-block;
            padding: 12px 30px;
            background-color: #ef4f5f;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-weight: 600;
            transition: background 0.3s;
        }
        .btn-login:hover { background-color: #d63d4d; }
    </style>
</head>
<body>

    <div class="card">
        <div class="success-icon">✓</div>
        <h1>Registration Successful!</h1>
        <p>Welcome to Food Auto, ${param.name}! Your account is ready.</p>
        <a href="Login.html" class="btn-login">Go to Login</a>
    </div>

</body>
</html>