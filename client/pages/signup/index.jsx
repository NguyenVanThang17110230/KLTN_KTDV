import Head from "next/head";
import Link from "next/link";
import React, { useEffect } from "react";
import * as Yup from "yup";
import { Formik, Field, Form } from "formik";

import { accountService } from "../../package/RestConnector";
import UserGuest from "../../layouts/UserGuest";
const SignUp = () => {
  useEffect(() => {
    const fetchData = async () => {
      const data = await accountService.wellcomeLoginUser();
      console.log("data", data);
    };
    fetchData();
  }, []);

  const SignupSchema = Yup.object().shape({
    firstName: Yup.string().required("First name required"),
    lastName: Yup.string().required("Last name required"),
    email: Yup.string().email("Invalid email").required("Your email required"),
    dob: Yup.date().required("Date of birth required"),
    gender: Yup.string().required("Gender required"),
    phoneNumber: Yup.string().required("Phone number required"),
    userCode: Yup.string().required("User code required"),
    password: Yup.string()
      .min(8, "Your password is too short!")
      .max(30, "Your password is too Long!")
      .required("Your password required!"),
    confirmPassword: Yup.string()
      .min(8, "Your password is too short!")
      .max(30, "Your password is too Long!")
      .required("Your password required!"),
  });

  const handleSignup = async (values, actions) => {
    console.log("hhhhh", values);
    actions.setSubmitting(true);
    // try {
    //   console.log("aloooo");
    //   const user = await accountService.loginAdmin(values);
    //   console.log("userrrrrrrrrrr", user);
    // } catch (err) {
    //   console.log("agugu");
    //   actions.setSubmitting(false);
    // }
  };

  return (
    <div className="relative z-10 w-full h-full flex items-center justify-center">
      <Head>
        <title>login</title>
      </Head>
      <Formik
        initialValues={{
          firstName: "",
          lastName: "",
          email: "",
          dob: "",
          gender: "Male",
          phoneNumber: "",
          userCode: "",
          password: "",
          confirmPassword:""
        }}
        validationSchema={SignupSchema}
        onSubmit={handleSignup}
      >
        {(props) => (
          <div className="w-full max-w-md">
            <Form
              onSubmit={props.handleSubmit}
              className="bg-white shadow-md rounded-md px-8 pt-6 pb-8 mb-4"
            >
              <h1 className="text-gray-600 font-bold text-2xl text-center">
                Sign Up
              </h1>
              <div className="grid grid-cols-2 gap-2 box-border mt-3">
                <div className="mb-4">
                  <label
                    className="block text-gray-700 text-sm font-bold mb-2"
                    htmlFor="firstname"
                  >
                    First name
                  </label>
                  <Field
                    id="firstName"
                    name="firstName"
                    placeholder="Thang"
                    className={
                      "appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm " +
                      (props.errors.firstName && props.touched.firstName
                        ? "border-red-500"
                        : "border-green-500")
                    }
                    type="text"
                  />
                  {props.errors.firstName && props.touched.firstName ? (
                  <div className="text-red-600 text-sm mt-2">
                    {props.errors.firstName}
                  </div>
                ) : null}
                </div>

                <div className="mb-4">
                  <label
                    className="block text-gray-700 text-sm font-bold mb-2"
                    htmlFor="lastname"
                  >
                    Last Name
                  </label>
                  <Field
                    id="lastName"
                    name="lastName"
                    placeholder="Nguyen"
                    className={
                      "appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm " +
                      (props.errors.lastName && props.touched.lastName
                        ? "border-red-500"
                        : "border-green-500")
                    }
                    type="text"
                  />
                  {props.errors.lastName && props.touched.lastName ? (
                  <div className="text-red-600 text-sm mt-2">
                    {props.errors.lastName}
                  </div>
                ) : null}
                </div>
              </div>

              <div className="mb-4">
                <label
                  className="block text-gray-700 text-sm font-bold mb-2"
                  htmlFor="email"
                >
                  Email
                </label>
                <Field
                  id="email"
                  name="email"
                  placeholder="demo@student.hcmute.edu.vn"
                  className={
                    "appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm " +
                    (props.errors.email && props.touched.email
                      ? "border-red-500"
                      : "border-green-500")
                  }
                  type="email"
                />
                {props.errors.email && props.touched.email ? (
                  <div className="text-red-600 text-sm mt-2">
                    {props.errors.email}
                  </div>
                ) : null}
              </div>
              <div className="mb-4">
                <label
                  className="block text-gray-700 text-sm font-bold mb-2"
                  htmlFor="dob"
                >
                  Date of Birth
                </label>
                <Field
                  id="dob"
                  name="dob"
                  placeholder="example@hcmute.edu.vn"
                  className={
                    "appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm " +
                    (props.errors.dob && props.touched.dob
                      ? "border-red-500"
                      : "border-green-500")
                  }
                  type="date"
                />
                {props.errors.dob && props.touched.dob ? (
                  <div className="text-red-600 text-sm mt-2">
                    {props.errors.dob}
                  </div>
                ) : null}
              </div>
              <div className="mb-4">
                <label
                  className="block text-gray-700 text-sm font-bold mb-2"
                  htmlFor="gender"
                >
                  Gender
                </label>
                <div role="group" aria-labelledby="my-radio-group">
                  <label className="mr-10">
                    <Field
                      className="mr-2 form-radio"
                      type="radio"
                      name="gender"
                      value="Male"
                      
                    />
                    Male
                  </label>
                  <label>
                    <Field
                      className="mr-2 form-radio"
                      type="radio"
                      name="gender"
                      value="Female"
                    />
                    Female
                  </label>
                </div>
              </div>
              <div className="grid grid-cols-2 gap-2 box-border mt-3">
                <div className="mb-4">
                  <label
                    className="block text-gray-700 text-sm font-bold mb-2"
                    htmlFor="phoneNumber"
                  >
                    Phone number
                  </label>
                  <Field
                    id="phoneNumber"
                    name="phoneNumber"
                    placeholder="0978686868"
                    className={
                      "appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm " +
                      (props.errors.phoneNumber && props.touched.phoneNumber
                        ? "border-red-500"
                        : "border-green-500")
                    }
                    type="text"
                  />
                  {props.errors.phoneNumber && props.touched.phoneNumber ? (
                  <div className="text-red-600 text-sm mt-2">
                    {props.errors.phoneNumber}
                  </div>
                ) : null}
                </div>

                <div className="mb-4">
                  <label
                    className="block text-gray-700 text-sm font-bold mb-2"
                    htmlFor="userCode"
                  >
                    User Code
                  </label>
                  <Field
                    id="userCode"
                    name="userCode"
                    placeholder="17110230"
                    className={
                      "appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm " +
                      (props.errors.userCode && props.touched.userCode
                        ? "border-red-500"
                        : "border-green-500")
                    }
                    type="text"
                  />
                  {props.errors.userCode && props.touched.userCode ? (
                  <div className="text-red-600 text-sm mt-2">
                    {props.errors.userCode}
                  </div> ) : null}
                </div>
              </div>
              <div className="grid grid-cols-2 gap-2 box-border mt-3">
                <div className="mb-4">
                  <label
                    className="block text-gray-700 text-sm font-bold mb-2"
                    htmlFor="password"
                  >
                    Password
                  </label>
                  <Field
                    id="password"
                    name="password"
                    placeholder="********"
                    className={
                      "appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm " +
                      (props.errors.password && props.touched.password
                        ? "border-red-500"
                        : "border-green-500")
                    }
                    type="password"
                  />
                  {props.errors.password && props.touched.password ? (
                  <div className="text-red-600 text-sm mt-2">
                    {props.errors.password}
                  </div> ) : null}
                </div>
                <div className="mb-4">
                  <label
                    className="block text-gray-700 text-sm font-bold mb-2"
                    htmlFor="confirmPassword"
                  >
                    Confirm Password
                  </label>
                  <Field
                    id="confirmPassword"
                    name="confirmPassword"
                    placeholder="********"
                    className={
                      "appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none text-sm " +
                      (props.errors.confirmPassword && props.touched.confirmPassword
                        ? "border-red-500"
                        : "border-green-500")
                    }
                    type="password"
                  />
                  {props.errors.confirmPassword && props.touched.confirmPassword ? (
                  <div className="text-red-600 text-sm mt-2">
                    {props.errors.confirmPassword}
                  </div> ) : null}
                </div>
              </div>
              <div className="flex items-center justify-between">
                <button
                  className="bg-gradient-to-r from-green-400 to-blue-500 hover:from-pink-500 hover:to-yellow-500 text-white py-2 px-10 rounded focus:outline-none focus:shadow-outline font-semibold"
                  type="submit"
                >
                  Sign up
                </button>
                <Link href="/login">
                  <a
                    className="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800"
                    href="#"
                  >
                    Login
                  </a>
                </Link>
              </div>
            </Form>
          </div>
        )}
      </Formik>
    </div>
  );
};
SignUp.getLayout = function getLayout(page) {
  return <UserGuest>{page}</UserGuest>;
};
export default SignUp;
